package com.tdedsh.dto.mapper;

import com.tdedsh.dto.CreateUserDto;
import com.tdedsh.dto.UserDto;
import com.tdedsh.generated.tables.records.UsersRecord;

public class UserMapper {
    // Convert UsersRecord to User DTO
    public static UserDto toUserDTO(UsersRecord record) {
        return new UserDto(
                record.getId(),
                record.getUsername(),
                record.getEmail(),
                record.getPasswordHash()
        );
    }

    // Convert User DTO to UsersRecord
    public static UsersRecord toUsersRecord(UserDto user) {
        UsersRecord record = new UsersRecord();
        record.setId(user.getId());
        record.setUsername(user.getUsername());
        record.setEmail(user.getEmail());
        record.setPasswordHash(user.getPasswordHash());
        return record;
    }

    public static UsersRecord createDtoToUserRecord(CreateUserDto dto) {
        UsersRecord record = new UsersRecord();
        record.setId(0);
        record.setUsername(dto.getUserName());
        record.setEmail(dto.getEmail());
        record.setPasswordHash(dto.getPassword());
        return record;
    }
}
