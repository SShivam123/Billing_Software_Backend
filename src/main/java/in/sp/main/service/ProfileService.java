package in.sp.main.service;
import java.util.UUID;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.sp.main.Enums.Role;
import in.sp.main.Exceptions.EmailAllReadyExistException;
import in.sp.main.Exceptions.GstAllreadyExistExceptipn;
import in.sp.main.Exceptions.UserAlreadyExistEception;
import in.sp.main.entity.ShopEntity;
import in.sp.main.entity.UserEntity;
import in.sp.main.io.ProfileRequest;
import in.sp.main.io.ProfileResponse;
import in.sp.main.io.adminUpdateRequest;
import in.sp.main.repository.ShopRepository;
import in.sp.main.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {
	private final UserRepository userRepository;
	private final ShopRepository shopRepository;
	private final PasswordEncoder passwordEncoder;
	public ProfileResponse createProfile(ProfileRequest request) {
		if(userRepository.existsByEmail(request.getEmail())) {
			throw new UserAlreadyExistEception("User With this email " + request.getEmail() + " Already Exists");
		}
		if(shopRepository.existsByGSTNumber(request.getGSTNumber()) && request.getGSTNumber() != null) {
			throw new GstAllreadyExistExceptipn("Shop With this GST " + request.getGSTNumber() + " Already Exists");
		}

		ShopEntity shopEntity = convertToShopEntity(request);
		shopEntity = shopRepository.save(shopEntity);
		UserEntity userEntity = convertToEntity(request, shopEntity);
		userEntity = userRepository.save(userEntity);
		return convertToResponse(userEntity);
	}

	private ShopEntity convertToShopEntity(ProfileRequest request) {
		ShopEntity shopEntity = ShopEntity.builder()
				.shopId(UUID.randomUUID().toString())
				.city(request.getCity())
				.shopAddress(request.getShopAddress())
				.pincode(request.getPincode())
				.GSTNumber(
					    request.getGSTNumber() == null ||
					    request.getGSTNumber().trim().isEmpty()
					        ? null
					        : request.getGSTNumber()
					)
				.shopName(request.getShopName())
				.state(request.getState())
				.build();
		return shopEntity;

	}

	private ProfileResponse convertToResponse(UserEntity entity) {
		ProfileResponse response = ProfileResponse.builder()
				.name(entity.getName())
				.email(entity.getEmail())
				.shopId(entity.getShop().getShopId())
				.shopName(entity.getShop().getShopName())
				.userId(entity.getUserId())
				.shopAddress(entity.getShop().getShopAddress())
				.GSTIN(entity.getShop().getGSTNumber())
				.role(entity.getRole().name())
				.build();
		return response;
	}

	private UserEntity convertToEntity(ProfileRequest request, ShopEntity shop) {
		UserEntity entity = UserEntity.builder()
				.name(request.getOwnerName())
				.email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword()))
				.userId(UUID.randomUUID().toString())
				.role(Role.ADMIN)
				.shop(shop)
				.build();
		return entity;
	}

	public ProfileResponse getUserProfile(String email) {
		UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not Exist this email"+email));
		return convertToResponse(userEntity);
	}

	@Transactional
	public ProfileResponse editprofile(adminUpdateRequest request, String email) {
		UserEntity adminUser = userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("user not found with this email "+email));
		ShopEntity shop = adminUser.getShop();
		
		adminUser.setName(request.getName());
		shop.setShopName(request.getShopName());
		shop.setShopAddress(request.getAddress());
		
		if(!email.equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
			throw new EmailAllReadyExistException("This email already exist "+request.getEmail());	
		}
		if(!email.equals(request.getEmail())) {
			adminUser.setEmail(request.getEmail());
		}
		return convertToResponse(adminUser);
	}
}