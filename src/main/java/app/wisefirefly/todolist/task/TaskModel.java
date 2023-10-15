package app.wisefirefly.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "tb_tasks")
public class TaskModel {

  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  private String description;
  @Column(length = 50)

  private String title;
  
  private LocalDateTime startAT;
  
  private LocalDateTime endAT;
  
  private String priority;

   @CreationTimestamp
   private LocalDateTime createAt;
  
   private UUID idUser;

  public void setTitle(String title) throws IllegalArgumentException{
    if(title.length() > 50) {
      throw new IllegalArgumentException("Titulo deve conter até 50 caracteres.");
    }
    this.title = title;
  }
}
