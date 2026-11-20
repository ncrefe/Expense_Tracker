package com.project.oba.model.response.user;

import com.project.oba.entity.User;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class UserResponse {

    private Long id;
    private String name;

    public static UserResponse fromEntity(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getUserName())
                .build();
    }

}
