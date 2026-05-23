package in.sp.main.service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import in.sp.main.entity.ItemEntity;
import in.sp.main.entity.ShopEntity;
import in.sp.main.entity.StockEntity;
import in.sp.main.entity.UserEntity;
import in.sp.main.io.ItemResponse;
import in.sp.main.io.StockRequest;
import in.sp.main.repository.ItemRepository;
import in.sp.main.repository.StockRepository;
import in.sp.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockService {
	private final UserRepository userRepository;
	private final StockRepository stockRepository;
	private final ItemRepository itemRepository;

	public ItemResponse addStock(StockRequest request, String email) {
		System.out.println(request.getItemId());
		UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found with this email " + email));
		ShopEntity shop = userEntity.getShop();
		ItemEntity existingItem = itemRepository.findByItemId(request.getItemId()).orElseThrow(()-> new RuntimeException("item in this id not exist"+request.getItemId()));
		if(existingItem.getCategory().getShop().getShopId().equals(shop.getId())) {
			throw new RuntimeException("Item and Shop are different");
		}
		
		StockEntity stock = stockRepository.findByItem(existingItem);
		stock.setAvilableStock(stock.getAvilableStock() == null ? 0 + request.getQuantity() : stock.getAvilableStock() + request.getQuantity() );
		stock.setItem(existingItem);
		existingItem.setStock(stock);
		ItemEntity savedEntity = itemRepository.save(existingItem);
		return convertToResponse(savedEntity);
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


}
