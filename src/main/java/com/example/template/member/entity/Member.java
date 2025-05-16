package com.example.template.member.entity;

import com.example.template.util.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "member", schema = "${schema.base}")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_member", nullable = false)
    private Long idMember;

    @Builder.Default
    @Column(name = "reg_date_member", nullable = false, updatable = false)
    private LocalDateTime regDateMember = LocalDateTime.now();

    @Column(name = "account_id", length = 20, nullable = false, unique = true)
    private String accountId;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "is_del_member", nullable = false)
    @Builder.Default
    private Boolean isDelMember = false;

    @Column(name = "deleted_date_member")
    private LocalDateTime deletedDateMember;

}
