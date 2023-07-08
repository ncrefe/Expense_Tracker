package com.project.oba.model.request.auth;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
@ToString
public class LoginRequest {

    @NotEmpty
    private String userName;

    @Length(min = 6)
    private String password;

}
