package in.sp.main.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.RazorpayException;

import in.sp.main.io.OrderResponse;
import in.sp.main.io.RazorpayOrderResponse;
import in.sp.main.io.RazorpayRequest;
import in.sp.main.io.paymentVerificationRequest;
import in.sp.main.service.OrderService;
import in.sp.main.service.RazorpayService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/payments")
public class PaymentController {
	private final RazorpayService razorpayService;
	private final OrderService orderService;

	@PostMapping("/create-order")
	public ResponseEntity<RazorpayOrderResponse> createRazorpayOrder(@RequestBody RazorpayRequest request) throws RazorpayException {
	  return new ResponseEntity<>( razorpayService.createOrder(request.getAmount(),request.getCurrency()), HttpStatus.CREATED);
	}

	@PostMapping("/verify")
	public OrderResponse verifyPayment(@RequestBody paymentVerificationRequest request) {
		return orderService.verifyPayment(request);
	}
}
