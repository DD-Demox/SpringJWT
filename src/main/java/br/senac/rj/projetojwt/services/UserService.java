package br.senac.rj.projetojwt.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.senac.rj.projetojwt.dto.CreateUserDto;
import br.senac.rj.projetojwt.dto.LoginUserDto;
import br.senac.rj.projetojwt.dto.RecoveryJwtTokenDto;
import br.senac.rj.projetojwt.entities.Role;
import br.senac.rj.projetojwt.entities.User;
import br.senac.rj.projetojwt.repositories.UserRepository;
import br.senac.rj.projetojwt.security.auth.JwtTokenService;
import br.senac.rj.projetojwt.security.config.SecurityConfiguration;
import br.senac.rj.projetojwt.security.userdetails.UserDetailsImpl;

@Service
public class UserService {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenService jwtTokenService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private SecurityConfiguration securityConfiguration;
	
	public RecoveryJwtTokenDto authenticateUser(LoginUserDto loginUserDto) {
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
				new UsernamePasswordAuthenticationToken(loginUserDto.email(), loginUserDto.password());
		
		Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
		
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
		
		return new RecoveryJwtTokenDto(jwtTokenService.generateToken(userDetailsImpl));
	}
	
	public void createUser(CreateUserDto createUserDto) {
		User user = new User();
		user.setEmail(createUserDto.email());
		user.setPassword(securityConfiguration.passwordEncoder().encode(createUserDto.password()));
		Role role = new Role();
		role.setName(createUserDto.role());
		List<Role> roles = new ArrayList<>();
		roles.add(role);
		user.setRoles(roles);
		userRepository.save(user);
		
	}
	
}
