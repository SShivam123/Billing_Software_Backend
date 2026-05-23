package in.sp.main.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.sp.main.io.DashboardResponse;
import in.sp.main.io.OrderResponse;
import in.sp.main.service.OrderService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashBoardController {
	private final OrderService orderService;

	@GetMapping("/")
	public DashboardResponse getDashBoardData() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		LocalDate today = LocalDate.now();
		 Double totalSale = orderService.sumSalesByDate(today,email);
		 Long totalOrders = orderService.countByOrderDate(today,email);
		 List<OrderResponse> recentOrder = orderService.findRecentOrder(email);
		 return new DashboardResponse(totalSale!=null ? totalSale : 0.0,totalOrders!=null ? totalOrders : 0,recentOrder);
	}
}
