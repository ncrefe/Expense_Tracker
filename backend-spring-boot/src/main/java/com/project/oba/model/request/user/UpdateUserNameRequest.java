package com.project.oba.model.request.user;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Data
@ToString
public class UpdateUserNameRequest {

    @NotEmpty(message = "Name can not be null!")
    private String name;

}
