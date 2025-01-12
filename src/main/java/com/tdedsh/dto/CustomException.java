package com.tdedsh.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomException extends RuntimeException{
    private int status;
    public CustomException(int status,String message){
        super(message);
        this.status = status;
    }
}
