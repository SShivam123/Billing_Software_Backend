package in.sp.main.service;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import in.sp.main.io.RazorpayOrderResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RazorpayService {
	@Value("${razorpay.key.id}")
	private String razorpayKetId;
	@Value("${razorpay.key.secret}")
	private String razorpayKeySecret;

	public RazorpayOrderResponse createOrder(Double amount,String currency) throws RazorpayException {
		RazorpayClient client = new RazorpayClient(razorpayKetId, razorpayKeySecret);
		JSONObject orderRequest = new JSONObject();
		orderRequest.put("amount",amount*100);
		orderRequest.put("currency",currency);
		orderRequest.put("receipt","order_reciept"+System.currentTimeMillis());
		orderRequest.put("payment_capture",1);

		Order order = client.orders.create(orderRequest);
		return convertToResponse(order);
	}

	private RazorpayOrderResponse convertToResponse(Order order) {
		return RazorpayOrderResponse.builder()
		.id(order.get("id"))
		.entity(order.get("entity"))
		.amount(((Number) order.get("amount")).doubleValue())
		.currency(order.get("currency"))
		.createAt(order.get("created_at"))
		.receipt(order.get("receipt"))
		.status(order.get("status"))
		.build();
	}
}
