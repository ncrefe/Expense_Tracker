package com.project.oba.model.response.user;

import com.project.oba.entity.User;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class FriendshipRequestsResponse {

    private Long id;
    private String name;

    public static FriendshipRequestsResponse fromEntity(User user) {
        return FriendshipRequestsResponse.builder()
                .id(user.getId())
                .name(user.getUserName())
                .build();
    }

}
