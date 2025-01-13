package com.tdedsh.dto.mapper;

import com.tdedsh.dto.TaskDto;
import com.tdedsh.generated.tables.records.TasksRecord;

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
                record.getTextColor(),
                record.getCreatedAt(),
                record.getUpdatedAt()
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
