package br.senac.rj.projetojwt.dto;

import java.util.List;

import br.senac.rj.projetojwt.entities.Role;

public record RecoveryUserDto(

        Long id,
        String email,
        List<Role> roles

) {
}