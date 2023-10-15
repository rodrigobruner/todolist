package app.wisefirefly.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.wisefirefly.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/Tasks")
public class TaskController {
  
  @Autowired
  private ITaskRepository taskRepository;

  @PostMapping("/")
  public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
    var idUser = request.getAttribute("idUser");
    taskModel.setIdUser((UUID) idUser);
    
    var currentDate = LocalDateTime.now();
    if(currentDate.isAfter(taskModel.getStartAT()) || currentDate.isAfter(taskModel.getEndAT())){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data de início ou de termino dever ser maior que a data atual.");
    }

    if(taskModel.getStartAT().isAfter(taskModel.getEndAT())){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data de início tem que ser menor que a data e fim.");
    }

    var task = this.taskRepository.save(taskModel);
    return ResponseEntity.status(HttpStatus.OK).body(task);
  }

  @GetMapping("/")
  public List<TaskModel> list( HttpServletRequest request){
    var idUser = request.getAttribute("idUser");
    var tasks = this.taskRepository.findByIdUser((UUID) idUser);
    return tasks;

  }

  @PutMapping("/{id}")
  public ResponseEntity update(@RequestBody TaskModel taskModel, @PathVariable UUID id, HttpServletRequest request){
    var idUser = request.getAttribute("idUser");
    var task = this.taskRepository.findById(id).orElse(null);

    if(task == null){
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa não encontrada.");
    }

    if(task.getIdUser().equals(idUser) == false){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário sem permissão para alterar esta tarefa.");
    }
    
    Utils.copyNonNullProperties(taskModel,task);

    return ResponseEntity.ok().body(this.taskRepository.save(task));
  }
}
