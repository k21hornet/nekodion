package com.konekokonekone.nekodion.user.mapper;

import com.konekokonekone.nekodion.user.dto.UserDto;
import com.konekokonekone.nekodion.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);
}
