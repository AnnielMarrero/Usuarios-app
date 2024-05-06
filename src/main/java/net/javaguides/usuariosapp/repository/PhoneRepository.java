package net.javaguides.usuariosapp.repository;

import net.javaguides.usuariosapp.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PhoneRepository extends JpaRepository<Phone, UUID> {

}
