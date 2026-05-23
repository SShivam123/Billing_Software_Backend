package in.sp.main.io;

import java.time.LocalDateTime;
import java.util.List;

import in.sp.main.Enums.PaymentMethod;
import in.sp.main.entity.PaymentDetailEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
	private String orderId;
	private String customerName;
	private String mobileNumber;
	private List<OrderResponse.orderItemResponse> cartItems;
	private Double subTotal;
	private Double tax;
	private Double grandTotal;
	private PaymentMethod paymentMethod;
	private LocalDateTime createAt;
	private PaymentDetailEntity paymentDetail;


	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class orderItemResponse{
		private String itemID;
		private String name;
		private Double price;
		private Integer quantity;
	}
}