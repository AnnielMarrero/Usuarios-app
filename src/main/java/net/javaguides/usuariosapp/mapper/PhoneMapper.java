package net.javaguides.usuariosapp.mapper;

import net.javaguides.usuariosapp.dto.PhoneDto;
import net.javaguides.usuariosapp.entity.Phone;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PhoneMapper {
    @Autowired
    private ModelMapper modelMapper;

    public Phone mapToPhone(PhoneDto phoneDto){
        return modelMapper.map(phoneDto, Phone.class);
    }

    public PhoneDto mapToPhoneDto(Phone phone){
        return modelMapper.map(phone, PhoneDto.class);
    }
}
