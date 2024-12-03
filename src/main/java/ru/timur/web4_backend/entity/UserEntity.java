package ru.timur.web4_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "user_data")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "salt", nullable = false)
    private String salt;

    @Lob
    @Column(name = "avatar")
    private byte[] avatar;

    @Column(name="avatar_type")
    private String avatarType;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<PointEntity> points;
}
