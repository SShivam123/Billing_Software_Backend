package in.sp.main.io;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RazorpayOrderResponse {
	private String id;
	private String entity;
	private Double amount;
	private String currency;
	private String status;
	private Date createAt;
	private String receipt;
}
