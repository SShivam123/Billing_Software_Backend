package in.sp.main.service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.sp.main.Exceptions.CategoryAlreadyExistsException;
import in.sp.main.entity.CategoryEntity;
import in.sp.main.entity.ShopEntity;
import in.sp.main.entity.UserEntity;
import in.sp.main.io.CategoryRequest;
import in.sp.main.io.CategoryResponse;
import in.sp.main.repository.CategoryRepository;
import in.sp.main.repository.ItemRepository;
import in.sp.main.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryRepository categoryRepository;
	private final FileUploadService fileUploadService;
	private final ItemRepository itemRepository;
	private final UserRepository userRepository;

	public CategoryResponse addCategory(CategoryRequest request,MultipartFile file,String email){
		UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found with this email " + email));
		ShopEntity shop = userEntity.getShop();
		if(categoryRepository.existsByNameAndShop(request.getName(), shop)) {
			throw new CategoryAlreadyExistsException("Category Already Exists with this name and shop");
		}
		CategoryEntity entity = convertToEntity(request,shop);
		entity.setImageUrl(fileUploadService.uploadFile(file));
		categoryRepository.save(entity);
		return convertToResponse(entity);
	}

	private CategoryResponse convertToResponse(CategoryEntity entity) {
		Integer items = itemRepository.countBycategoryId(entity.getId());
		CategoryResponse response = CategoryResponse.builder()
				.bgColor(entity.getBgColor())
				.name(entity.getName())
				.categoryId(entity.getCategoryId())
				.createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdatedAt())
				.description(entity.getDescription())
				.imageUrl(entity.getImageUrl())
				.item(items)
				.shopId(entity.getShop().getShopId())
				.build();
		return response;
	}

	private CategoryEntity convertToEntity(CategoryRequest request,ShopEntity shop) {
		CategoryEntity entity = CategoryEntity.builder()
				.categoryId(UUID.randomUUID().toString())
				.bgColor(request.getBgColor())
				.description(request.getDescription())
				.name(request.getName())
				.shop(shop)
				.build();
		return entity;
	}

	public List<CategoryResponse> getAllCategory(String email){
		UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found with this email " + email));
		ShopEntity shop = userEntity.getShop();
		List<CategoryEntity> allcategory = categoryRepository.findByShop(shop);
		return ConvertToListResponse(allcategory);
	}
	private List<CategoryResponse> ConvertToListResponse(List<CategoryEntity> allEntity) {
		List<CategoryResponse> allResponse = new ArrayList<>();
	    for(CategoryEntity entity : allEntity) {
	    	Integer items = itemRepository.countByCategory(entity);
	    	CategoryResponse response = CategoryResponse.builder()
	    			.bgColor(entity.getBgColor())
					.name(entity.getName())
					.item(items)
					.categoryId(entity.getCategoryId())
					.createdAt(entity.getCreatedAt())
					.updatedAt(entity.getUpdatedAt())
					.description(entity.getDescription())
					.imageUrl(entity.getImageUrl())
					.shopId(entity.getShop().getShopId())
					.build();
	    	allResponse.add(response);
	    }

		return allResponse;
	}

	public void deleteCategory(String id) {
		CategoryEntity entity = categoryRepository.findByCategoryId(id).orElseThrow(()-> new EntityNotFoundException("Category not found"));
		if(entity!=null) {
			fileUploadService.deleteFile(entity.getImageUrl());
			categoryRepository.delete(entity);
		}else {
			throw new EntityNotFoundException("Category not found"+id);
		}
	}

	public CategoryResponse updatecategory(String id,CategoryRequest request,MultipartFile file) {
		CategoryEntity entity = categoryRepository.findByCategoryId(id).orElseThrow(()-> new RuntimeException("Category not found"));
		if(entity != null) {
			entity.setBgColor(request.getBgColor());
			entity.setDescription(request.getDescription());
			entity.setName(request.getName());
			fileUploadService.deleteFile(entity.getImageUrl());
			entity.setImageUrl(fileUploadService.uploadFile(file));
			categoryRepository.save(entity);
			CategoryResponse response = convertToResponse(entity);
			return response;
		}else {
			throw new EntityNotFoundException("Category not found"+id);
		}
	}

}
