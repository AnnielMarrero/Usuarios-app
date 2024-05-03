package net.javaguides.usuariosapp.mapper;

import net.javaguides.usuariosapp.dto.UserDto;
import net.javaguides.usuariosapp.entity.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    @Autowired
    private ModelMapper modelMapper;

    public User mapToUser(UserDto userDto){
        return modelMapper.map(userDto, User.class);
    }

    public UserDto mapToUserDto(User user){
        user.setPassword(null);
        return modelMapper.map(user, UserDto.class);
    }
}
