package com.project.oba.model.request.auth;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@ToString
public class RegisterRequest {

    @NotEmpty(message = "User name can not be null!")
    private String userName;

    @Size(min = 6)
    @NotEmpty(message = "Password can not be null!")
    private String password;

    @Size(min = 6)
    @NotEmpty(message = "Password can not be null!")
    private String rewritePassword;

}