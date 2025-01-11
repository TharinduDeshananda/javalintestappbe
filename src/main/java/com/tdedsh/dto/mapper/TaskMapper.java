package com.tdedsh.dto.mapper;

import com.tdedsh.dto.TaskDto;
import com.tdedsh.dto.UserDto;
import com.tdedsh.generated.tables.records.TasksRecord;
import com.tdedsh.generated.tables.records.UsersRecord;

public class TaskMapper {
    // Convert TasksRecord to Task DTO
    public static TaskDto toTaskDTO(TasksRecord record) {
        return new TaskDto(
                record.getId(),
                record.getUserId(),
                record.getTitle(),
                record.getDescription(),
                record.getStatus(),
                record.getBackgroundColor(),
                record.getTextColor()
        );
    }

    // Convert Task DTO to TasksRecord
    public static TasksRecord toTasksRecord(TaskDto task) {
        TasksRecord record = new TasksRecord();
        record.setId(task.getId());
        record.setUserId(task.getUserId());
        record.setTitle(task.getTitle());
        record.setDescription(task.getDescription());
        record.setStatus(task.getStatus());
        record.setBackgroundColor(task.getBackgroundColor());
        record.setTextColor(task.getTextColor());
        return record;
    }
}
