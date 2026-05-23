package in.sp.main.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import in.sp.main.entity.UserEntity;
import in.sp.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppUserDetailService implements UserDetailsService{

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity existingUser = userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User not found this email: "+email));
		return new User(existingUser.getEmail(),existingUser.getPassword(),Collections
				.singleton(new SimpleGrantedAuthority(("ROLE_"+existingUser.getRole().name()))));
	}

}
