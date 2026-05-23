package in.sp.main.io;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockRequest {
	@NotBlank(message ="item id connot be null and empty")
	private String itemId;
//	@NotNull(message = "quantity can not be null")
	@Positive(message = "quntity must be greater than Zero")
	private Integer quantity;
}
