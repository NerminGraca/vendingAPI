package ba.ngraca.vending.controllers;

import ba.ngraca.vending.models.User;
import ba.ngraca.vending.payload.request.LoginRequest;
import ba.ngraca.vending.payload.request.SignupRequest;
import ba.ngraca.vending.payload.response.JwtResponse;
import ba.ngraca.vending.payload.response.MessageResponse;
import ba.ngraca.vending.repository.UserRepository;
import ba.ngraca.vending.security.jwt.JwtUtils;
import ba.ngraca.vending.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody final LoginRequest loginRequest) {

		final Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		final String jwt = jwtUtils.generateJwtToken(authentication);
		
		final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		final List<String> roles = userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());

		return ResponseEntity.ok(
				new JwtResponse(
						jwt,
						userDetails.getId(),
						userDetails.getUsername(),
						roles
				)
		);
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		final User user = new User(
				signUpRequest.getUsername(),
				encoder.encode(signUpRequest.getPassword()),
				signUpRequest.getRole()
		);

		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse(signUpRequest.getRole() + " registered successfully!"));
	}
}
