package com.konekokonekone.nekodion.user.dto;

import lombok.Data;

@Data
public class UserDto {

    private String id;

    private String auth0Id;

    private String email;
}
