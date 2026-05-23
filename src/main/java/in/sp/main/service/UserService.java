package in.sp.main.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.sp.main.Enums.Role;
import in.sp.main.Exceptions.EmailAllReadyExistException;
import in.sp.main.entity.ShopEntity;
import in.sp.main.entity.UserEntity;
import in.sp.main.io.ProfileResponse;
import in.sp.main.io.UserRequest;
import in.sp.main.io.UserResponse;
import in.sp.main.io.UserUpdateRequest;
import in.sp.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;


	public UserResponse createUser(UserRequest request,String adminEmail) {
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new EmailAllReadyExistException("Email Allready exist");
		}
		UserEntity adminUser = userRepository.findByEmail(adminEmail).orElseThrow(()->new UsernameNotFoundException("User not exist in this email"+adminEmail));
		ShopEntity shop = adminUser.getShop();
		UserEntity newUser = convertToEntity(request,shop);
		newUser = userRepository.save(newUser);
		return convertToResponse(newUser);
	}


	private UserEntity convertToEntity(UserRequest request,ShopEntity shop) {
		UserEntity entity = UserEntity.builder().userId(UUID.randomUUID().toString()).email(request.getEmail())
				.name(request.getName())
				.password(passwordEncoder
				.encode(request.getPassword()))
				.role(Role.USER)
				.shop(shop)
				.build();
		return entity;
	}


	private UserResponse convertToResponse(UserEntity newUser) {
		UserResponse response = UserResponse.builder().name(newUser.getName()).email(newUser.getEmail())
				.createAt(newUser.getCreateAt()).updateAt(newUser.getUpdateAt()).role(newUser.getRole())
				.userId(newUser.getUserId()).build();
		return response;
	}


	public Role getUserRole(String email) {
		UserEntity user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found this email: " + email));
		return user.getRole();
	}


	public List<UserResponse> readUser(String adminEmail) {
		UserEntity adminUser = userRepository.findByEmail(adminEmail).orElseThrow(()->new UsernameNotFoundException("User not exist in this email"+adminEmail));
		ShopEntity shop = adminUser.getShop();
		return userRepository.findByShop(shop)
				.stream()
				.filter(user -> user.getRole().name()!="ADMIN")
				.map(user -> convertToResponse(user))
				.collect(Collectors.toList());
	}


	public void deleteUser(String id , String adminEmail) {
		UserEntity adminUser = userRepository.findByEmail(adminEmail).orElseThrow(()->new UsernameNotFoundException("User not exist in this email"+adminEmail));
		ShopEntity shop = adminUser.getShop();
		UserEntity user = userRepository.findByUserIdAndShop(id,shop)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		userRepository.delete(user);
	}


	public ProfileResponse updateUser(UserUpdateRequest request, String email) {
		UserEntity employee = userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not exist in this email"+email));
		ShopEntity shop = employee.getShop();
		employee.setName(request.getName());
		userRepository.save(employee);
		return convertToResponse(employee,shop);
	}


	private ProfileResponse convertToResponse(UserEntity employee, ShopEntity shop) {
		ProfileResponse response = ProfileResponse.builder()
				.email(employee.getEmail())
				.GSTIN(shop.getGSTNumber())
				.name(employee.getName())
				.role(employee.getRole().name())
				.shopAddress(shop.getShopAddress())
				.userId(employee.getUserId())
				.shopId(shop.getShopId())
				.build();
		return response;
	}
}
