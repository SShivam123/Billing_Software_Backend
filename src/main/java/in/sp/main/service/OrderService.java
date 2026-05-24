package in.sp.main.service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import in.sp.main.Enums.PaymentMethod;
import in.sp.main.Exceptions.itemNotExistException;
import in.sp.main.entity.ItemEntity;
import in.sp.main.entity.OrderEntity;
import in.sp.main.entity.OrderItemEntity;
import in.sp.main.entity.PaymentDetailEntity;
import in.sp.main.entity.ShopEntity;
import in.sp.main.entity.StockEntity;
import in.sp.main.entity.UserEntity;
import in.sp.main.io.OrderRequest;
import in.sp.main.io.OrderResponse;
import in.sp.main.io.paymentVerificationRequest;
import in.sp.main.repository.ItemRepository;
import in.sp.main.repository.OrderRepository;
import in.sp.main.repository.StockRepository;
import in.sp.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;
	private final UserRepository userRepository;
	private final ItemRepository itemRepository;
	private final StockRepository stockRepository;

	public OrderResponse createOrder(OrderRequest request,String email) {
			UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("user not exist in this email "+email));
			ShopEntity shop = userEntity.getShop();
		OrderEntity newOrder = convertToOrderEntity(request);
		PaymentDetailEntity paymentDetail = new PaymentDetailEntity();
		paymentDetail.setStatus(newOrder.getPaymentMethod() == PaymentMethod.CASH ? PaymentDetailEntity.paymentStatus.COMPLETE: PaymentDetailEntity.paymentStatus.PENDEING);
		newOrder.setPaymentDetailEntity(paymentDetail);
		newOrder.setShop(shop);
		System.out.println(request.getCartItems());
		List<OrderItemEntity> orderItems = request.getCartItems().stream().map(this::convertToOrderItemEntity).collect(Collectors.toList());
		orderItems.forEach(item->item.setOrder(newOrder));
		newOrder.setOrderItems(orderItems);
	 	OrderEntity saveOrder = orderRepository.save(newOrder);
		return convertToResponse(saveOrder);
	}


	private OrderItemEntity convertToOrderItemEntity(OrderRequest.orderItemRequest 
	orderitemRequest) {
		ItemEntity itemEntity = itemRepository.findByItemId(orderitemRequest.getItemid()).orElseThrow(()->new itemNotExistException("Item no avilable.."+orderitemRequest.getItemid()));
		StockEntity existingStockEntity = stockRepository.findByItem(itemEntity);
		if(existingStockEntity.getAvilableStock() < orderitemRequest.getQuantity()) {
			throw new RuntimeException("Stock is not available");
		}
		existingStockEntity.setAvilableStock(existingStockEntity.getAvilableStock() - orderitemRequest.getQuantity());
		stockRepository.save(existingStockEntity);
		 return OrderItemEntity.builder()
		.itemId(orderitemRequest.getItemid())
		.name(orderitemRequest.getName())
		.price(orderitemRequest.getPrice())
		.quantity(orderitemRequest.getQuantity())
		.build();
	}

	private OrderResponse convertToResponse(OrderEntity newOrder) {
		return OrderResponse.builder()
				.customerName(newOrder
				.getCustomerName())
				.mobileNumber(newOrder.getMobileNumber())
				.tax(newOrder.getTax())
				.subTotal(newOrder.getSubTotal())
				.grandTotal(newOrder.getGrandTotal())
				.createAt(newOrder.getCreatedAt())
				.paymentDetail(newOrder.getPaymentDetailEntity())
				.orderId(newOrder.getOrderId())
				.paymentMethod(newOrder.getPaymentMethod())
				.cartItems(newOrder.getOrderItems().stream().map      (this::converToItemResponse).collect(Collectors.toList()))
				.build();
	}

	private OrderResponse.orderItemResponse converToItemResponse(OrderItemEntity orderItemEntity) {
		return OrderResponse.orderItemResponse.builder()
		.itemID(orderItemEntity.getItemId())
		.name(orderItemEntity.getName())
		.price(orderItemEntity.getPrice())
		.quantity(orderItemEntity.getQuantity())
		.build();
	}

	private OrderEntity convertToOrderEntity(OrderRequest request) {
		return OrderEntity.builder()
				.customerName(request.getCustomerName())
				.mobileNumber(request.getMobileNumber())
				.subTotal(request.getSubTotal())
				.Tax(request.getTax())
				.grandTotal(request.getGrandTotal())
				.paymentMethod(PaymentMethod.valueOf(request.getPaymentMode()))
				.build();
	}

	public void deleteByOrder(String orderId,String email) {
		UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("user not exist in this email "+email));
		ShopEntity shop = userEntity.getShop();
		OrderEntity orderEntity = orderRepository.findByOrderIdAndShop(orderId,shop).orElseThrow(()-> new UsernameNotFoundException("order not exist with this Id "+orderId));
		orderRepository.delete(orderEntity);
	}

	public Page<OrderResponse> getLatestOrders(String email,int page ,int size){
		UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("user not exist in this email "+email));
		ShopEntity shop = userEntity.getShop();
		Pageable pageable = PageRequest.of(page, size);
		Page<OrderEntity> latestOrders = orderRepository.findAllByShopOrderByCreatedAtDesc(shop,pageable);
		return latestOrders.map(this::convertToResponse);
	}

	public  OrderResponse verifyPayment(paymentVerificationRequest request) {
		OrderEntity existingOrder = orderRepository.findByOrderId(request.getOrderId()).orElseThrow(()-> new RuntimeException("order not found with thiod id"+request.getOrderId()));
		if(!verifyRazorpaySignature(request.getRazorpayOrderId(),request.getRazorpayPaymentId(),request.getRazorpaySignature())) {
			throw new RuntimeException("payment verification faild");
		}
		PaymentDetailEntity paymentDetail = existingOrder.getPaymentDetailEntity();
		paymentDetail.setRazorpayOrderId(request.getRazorpayOrderId());
		paymentDetail.setRazorpayPaymentId(request.getRazorpayPaymentId());
		paymentDetail.setRazorpaySignature(request.getRazorpaySignature());
		paymentDetail.setStatus(PaymentDetailEntity.paymentStatus.COMPLETE);

		orderRepository.save(existingOrder);
		return convertToResponse(existingOrder);
	}


	private boolean verifyRazorpaySignature(String razorpayOrderId, String razorpayPaymentId,
			String razorpaySignature) {
		return true;
	}

	public Double sumSalesByDate(LocalDate date,String email) {
		UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("user not exist in this email "+email));
		ShopEntity shop = userEntity.getShop();
		return orderRepository.sumSalesByDate(date,shop);
	}

	public Long countByOrderDate(LocalDate date,String email) {
		UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("user not exist in this email "+email));
		ShopEntity shop = userEntity.getShop();
		return orderRepository.countByOrderDate(date,shop);
	}

	public List<OrderResponse> findRecentOrder(String email){
		UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("user not exist in this email "+email));
		ShopEntity shop = userEntity.getShop();
		return orderRepository.findRecentOrder(PageRequest.of(0,5),shop)
				.stream()
				.map(this::convertToResponse)
				.collect(Collectors.toList());
	}
	
//	public Page<OrderEntity> getOrders(Long shopId, int page, int size){
//	    Pageable pageable = PageRequest.of(page, size);
//	    return orderRepository.findAllByShopOrderByCreatedAtDesc(shopId, pageable);
//	}
}
