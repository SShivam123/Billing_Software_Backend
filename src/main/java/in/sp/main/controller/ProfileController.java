package in.sp.main.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import in.sp.main.io.ProfileRequest;
import in.sp.main.io.ProfileResponse;
import in.sp.main.io.UserUpdateRequest;
import in.sp.main.io.adminUpdateRequest;
import in.sp.main.service.AppUserDetailService;
import in.sp.main.service.JwtService;
import in.sp.main.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProfileController {
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final AppUserDetailService userDetailsService;
	private final  ProfileService profileService;

	@PostMapping("/register")
	public ResponseEntity<ProfileResponse> register(@Valid @RequestBody ProfileRequest request)
			throws JsonProcessingException {
		ProfileResponse response = profileService.createProfile(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/profile")
	public ResponseEntity<ProfileResponse> getUserProfile(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		ProfileResponse respone = profileService.getUserProfile(email);
		return new ResponseEntity<>(respone,HttpStatus.OK);
	}
	
	@PutMapping("/admin/update")
	public ResponseEntity<ProfileResponse> editprofile(@RequestBody adminUpdateRequest request){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		ProfileResponse response = profileService.editprofile(request,email);
		return new ResponseEntity<ProfileResponse>(response,HttpStatus.OK);
	}
	
}