package br.senac.rj.projetojwt.security.auth;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.senac.rj.projetojwt.security.userdetails.UserDetailsImpl;

@Service
public class JwtTokenService {
	private static final String SECRET_KEY="secretoNaoContaParaNgm";
	private static final String ISSUER ="dd-demox";
	
	public String generateToken(UserDetailsImpl user) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
			return JWT.create()
					.withIssuer(ISSUER)
					.withIssuedAt(creationDate())
					.withExpiresAt(expirationDate())
					.withSubject(user.getUsername())
					.sign(algorithm);
		} catch (JWTVerificationException e) {
			throw new JWTCreationException("Erro ao gerar token.", e);
		}
	}
	
	 public String getSubjectFromToken(String token) {
	        try {
	            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
	            return JWT.require(algorithm)
	                    .withIssuer(ISSUER)
	                    .build()
	                    .verify(token)
	                    .getSubject();
	        } catch (JWTVerificationException exception){
	            throw new JWTVerificationException("Token inválido ou expirado.");
	        }
	    }
	
	private Instant creationDate() {
		return ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toInstant();
	}
	private Instant expirationDate() {
        return ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).plusHours(4).toInstant();
    }
}
