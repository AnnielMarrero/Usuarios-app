package net.javaguides.bankingapp.repository;

import net.javaguides.bankingapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

}
