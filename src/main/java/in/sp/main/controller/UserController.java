package in.sp.main.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.sp.main.io.ProfileResponse;
import in.sp.main.io.UserRequest;
import in.sp.main.io.UserResponse;
import in.sp.main.io.UserUpdateRequest;
import in.sp.main.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/admin")
public class UserController {

	private final UserService userService;

	@PostMapping("/admin/register")
	public ResponseEntity<UserResponse> registerUser(@RequestBody UserRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		return new ResponseEntity<>(userService.createUser(request,email),HttpStatus.CREATED);

	}

	@GetMapping("/admin/users")
	public List<UserResponse> getAllUaer(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		return userService.readUser(email);
	}

	@DeleteMapping("/admin/users/delete/{userid}")
	public ResponseEntity<?> deleteUser(@PathVariable String userid) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String adminEmail = auth.getName();

			userService.deleteUser(userid,adminEmail);
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}
	
	@PutMapping("/user/update")
	public ResponseEntity<ProfileResponse> updateUser(@RequestBody UserUpdateRequest request){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		return new ResponseEntity<ProfileResponse>(userService.updateUser(request,email),HttpStatus.OK);
	}
}
