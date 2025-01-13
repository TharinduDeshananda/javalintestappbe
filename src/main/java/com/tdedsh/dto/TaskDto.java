package com.tdedsh.dto;

import com.tdedsh.generated.enums.TasksStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data // Generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Generates a no-args constructor
@AllArgsConstructor // Generates an all-args constructor
public class TaskDto {
    private Integer id; // Use Integer to allow null values
    private Integer userId; // Foreign key to User
    private String title;
    private String description;
    private TasksStatus status; // Can be "TODO", "IN_PROGRESS", or "DONE"
    private String backgroundColor;
    private String textColor;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
