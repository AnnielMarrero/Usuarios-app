package net.javaguides.usuariosapp.service.impl;

import net.javaguides.usuariosapp.dto.PhoneDto;
import net.javaguides.usuariosapp.entity.Phone;
import net.javaguides.usuariosapp.mapper.PhoneMapper;
import net.javaguides.usuariosapp.repository.PhoneRepository;
import net.javaguides.usuariosapp.service.PhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhoneServiceImpl implements PhoneService {
    private final PhoneRepository phoneRepository;

    @Autowired
    private PhoneMapper phoneMapper;

    public PhoneServiceImpl(PhoneRepository phoneRepository) {
        this.phoneRepository = phoneRepository;
    }

    @Override
    public List<PhoneDto> addRange(List<PhoneDto> phoneDtos) {
        List<Phone> phones = phoneDtos.stream().map(phoneMapper::mapToPhone)
                .collect(Collectors.toList());

        List<Phone> savedPhones = phoneRepository.saveAll(phones);
        return savedPhones.stream().map(phoneMapper::mapToPhoneDto)
                .collect(Collectors.toList());
    }



}
