package br.senac.rj.projetojwt.security.auth;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.senac.rj.projetojwt.entities.User;
import br.senac.rj.projetojwt.repositories.UserRepository;
import br.senac.rj.projetojwt.security.config.SecurityConfiguration;
import br.senac.rj.projetojwt.security.userdetails.UserDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UserAuthFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenService jwtTokenService;
	
	@Autowired
	private UserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if(checkIfEndPointIsNotPublic(request)) {
			String token = recoveryToken(request);
			if(token!=null) {
				String subject = jwtTokenService.getSubjectFromToken(token);
				User user = userRepository.findByEmail(subject).get();
				UserDetailsImpl userDetails = new UserDetailsImpl(user);
				Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),null, userDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}else {
				throw new RuntimeException("O token está ausente");
			}
		}
		filterChain.doFilter(request, response);
		
	}
	
	private String recoveryToken(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		if(authHeader!= null) {
			return authHeader.replace("Bearer ","");
		}
		return null;
	}
	
	private boolean checkIfEndPointIsNotPublic(HttpServletRequest request) {
		String reqURI = request.getRequestURI();
		return !Arrays.asList(SecurityConfiguration.ENDPOINTS_WITH_AUTH_NOT_REQUIRED).contains(reqURI);
	}
	
}
