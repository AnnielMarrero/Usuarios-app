package net.javaguides.bankingapp.mapper;

import net.javaguides.bankingapp.dto.UserDto;
import net.javaguides.bankingapp.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    @Autowired
    private ModelMapper modelMapper;

    public User mapToUser(UserDto userDto){
        return modelMapper.map(userDto, User.class);
    }

    public UserDto mapToUserDto(User User){
        return modelMapper.map(User, UserDto.class);
    }
}
