package in.sp.main.io;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
	private String customerName;
	private String mobileNumber;
	private List<orderItemRequest> cartItems;
	private Double subTotal;
	private Double tax;
	private Double grandTotal;
	private String paymentMode;


	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class orderItemRequest{
		private String itemid;
		private String name;
		private Double price;
		private Integer quantity;
	}
}
