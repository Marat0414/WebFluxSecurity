package net.maratm.webfluxsecurity.mapper;

import net.maratm.webfluxsecurity.dto.UserDto;
import net.maratm.webfluxsecurity.entity.UserEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto map(UserEntity userEntity);

    @InheritInverseConfiguration
    UserEntity map(UserDto dto);


}
