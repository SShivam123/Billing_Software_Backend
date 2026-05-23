package in.sp.main.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class PaymentDetailEntity {
	private String razorpayOrderId;
	private String razorpayPaymentId;
	private String razorpaySignature;
	private paymentStatus status;
	public enum paymentStatus{
			PENDEING,COMPLETE,FAILED
	}
}
