package ru.study.webfluxsecurity.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import ru.study.webfluxsecurity.dto.UserDto;
import ru.study.webfluxsecurity.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {
     UserDto map(UserEntity userEntity);
     @InheritInverseConfiguration
     UserEntity map(UserDto dto);
}
