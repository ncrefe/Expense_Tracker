package com.project.oba.model.request.auth;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@ToString
public class ResetPasswordRequest {

    @NotNull(message = "Current password can not be null!")
    private String currentPassword;

    @Size(min = 6, message = "New Password must be at least 4 characters long!")
    @NotEmpty(message = "New Password can not be null!")
    private String newPassword;

    @Size(min = 6, message = "RewritePassword must be at least 4 characters long!")
    @NotEmpty(message = "RewritePassword Password can not be null!")
    private String rewritePassword;

}
