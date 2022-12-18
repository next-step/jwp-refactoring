package kitchenpos.order.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.ui.dto.OrderRequest;
import kitchenpos.order.ui.dto.OrderResponse;
import kitchenpos.order.ui.dto.OrderStatusRequest;

@RestController
public class OrderRestController {
	private final OrderService orderService;

	public OrderRestController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping("/api/orders")
	public ResponseEntity<OrderResponse> create(@RequestBody OrderRequest request) {
		OrderResponse created = new OrderResponse(
			orderService.create(
				request.toOrder()));

		URI uri = URI.create("/api/orders/" + created.getId());
		return ResponseEntity.created(uri)
							 .body(created);
	}

	@GetMapping("/api/orders")
	public ResponseEntity<List<OrderResponse>> list() {
		return ResponseEntity.ok()
							 .body(
								 OrderResponse.of(orderService.findAll()));
	}

	@PutMapping("/api/orders/{orderId}/order-status")
	public ResponseEntity<OrderResponse> changeOrderStatus(
		@PathVariable Long orderId,
		@RequestBody OrderStatusRequest statusRequest) {
		return ResponseEntity.ok(
			orderService.changeOrderStatus(
				orderId, statusRequest));
	}
}
