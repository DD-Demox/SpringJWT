package br.senac.rj.projetojwt.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.senac.rj.projetojwt.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
}
