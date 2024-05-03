package net.javaguides.usuariosapp.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "telefonos")
@Entity
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String number;
    private String cityCode;
    private String contryCode;

    @ManyToOne
    @JoinColumn(name="userId", nullable=false)
    private User user;
}
