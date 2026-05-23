package in.sp.main.service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import in.sp.main.Exceptions.ItemAlReadyExistException;
import in.sp.main.Exceptions.categoryNotFoundException;
import in.sp.main.Exceptions.itemNotExistException;
import in.sp.main.entity.CategoryEntity;
import in.sp.main.entity.ItemEntity;
import in.sp.main.entity.ShopEntity;
import in.sp.main.entity.StockEntity;
import in.sp.main.entity.UserEntity;
import in.sp.main.io.ItemRequest;
import in.sp.main.io.ItemResponse;
import in.sp.main.repository.CategoryRepository;
import in.sp.main.repository.ItemRepository;
import in.sp.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {
	private final CategoryRepository categoryRepository;
	private final ItemRepository itemRepository;
	private final FileUploadService fileUploadService;
	private final UserRepository userRepository;

	public ItemResponse addItem(ItemRequest request , MultipartFile file,String adminEmail){
		UserEntity adminUser = userRepository.findByEmail(adminEmail).orElseThrow(()->new UsernameNotFoundException("User not exist in this email"+adminEmail));
		ShopEntity shop = adminUser.getShop();
		CategoryEntity existingcategory = categoryRepository.findByCategoryId(request.getCategoryId()).orElseThrow(()-> new categoryNotFoundException("category is not found"+request.getCategoryId()));
		if(itemRepository.existsByNameIgnoreCaseAndCategoryId(request.getName(),existingcategory.getId())) {
			throw new ItemAlReadyExistException("Item is Already Exist within a name"+request.getName());
		}
		if(!existingcategory.getShop().getId().equals(shop.getId())) {
			throw new RuntimeException("Shop and category mismatch");
		}
		
		ItemEntity entity = convertToentity(request);
		StockEntity stockEntity = new StockEntity();
		stockEntity.setItem(entity);
		stockEntity.setAvilableStock(0);
		entity.setStock(stockEntity);
		entity.setImageurl(fileUploadService.uploadFile(file));
		entity.setCategory(existingcategory);
		itemRepository.save(entity);
		return convertToResponse(entity);
	}


	public List<ItemResponse> getAllItems(String email){
		UserEntity adminUser = userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not exist in this email"+email));
		ShopEntity shop = adminUser.getShop();
	     return itemRepository.FindAllItemByShop(shop.getId())
		.stream()
		.map(item->convertToResponse(item))
		.collect(Collectors.toList());
	}


	public void deleteItem(String itemId,String email) {
		ItemEntity itemEntity = itemRepository.findByItemId(itemId).orElseThrow(()->new itemNotExistException("Item no avilable.."+itemId));
		UserEntity adminUser = userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not exist in this email"+email));
		ShopEntity shop = adminUser.getShop();
		if(itemEntity.getCategory().getShop().getId() != shop.getId()) {
			throw new RuntimeException("Shop and Item does not match");
		}
		boolean isFileDeleted = fileUploadService.deleteFile(itemEntity.getImageurl());
		if(isFileDeleted) {
			itemRepository.delete(itemEntity);
		}
		else {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Unable to delete the image");
		}
	}


	private ItemResponse convertToResponse(ItemEntity entity) {
		ItemResponse response = ItemResponse.builder()
				.name(entity.getName())
				.price(entity.getPrice())
				.description(entity.getDescription())
				.createAt(entity.getCreateAt())
				.updateAt(entity.getUpdateAt())
				.imageurl(entity.getImageurl())
				.categoryName(entity.getCategory().getName())
				.categoryId(entity.getCategory().getCategoryId())
				.itemid(entity.getItemId())
				.stock(entity.getStock() == null ? 0 : entity.getStock().getAvilableStock())
				.build();
		return response;
	}


	private ItemEntity convertToentity(ItemRequest request) {
		ItemEntity entity = ItemEntity.builder()
				.name(request.getName())
				.price(request.getPrice())
				.description(request.getDescription())
				.itemId(UUID.randomUUID().toString())
				.build();
		return entity;
	}


}
