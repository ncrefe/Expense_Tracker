package com.project.oba.model.response.auth;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class LoginResponse {

    private Long id;
    private String token;

}
