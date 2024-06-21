package br.senac.rj.projetojwt.dto;

import br.senac.rj.projetojwt.enums.RoleName;

public record CreateUserDto(

        String email,
        String password,
        RoleName role

) {
}
