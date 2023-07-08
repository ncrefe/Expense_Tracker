package com.project.oba.exception;

public enum ErrorCode {

    task_not_found(404),
    validation(422),
    unauthorized(401),
    resource_missing(404),
    account_already_exists(409),
    account_missing(404),
    password_mismatch(409);

    private final int httpCode;

    ErrorCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public int getHttpCode() {
        return httpCode;
    }

    }
