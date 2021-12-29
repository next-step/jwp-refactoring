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
import kitchenpos.order.dto.OrderDto;
import kitchenpos.order.dto.OrderRequest;

@RestController
public class OrderRestController {
	private final OrderService orderService;

	public OrderRestController(final OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping("/api/orders")
	public ResponseEntity<OrderDto> create(@RequestBody OrderRequest request) {
		OrderDto order = orderService.create(request);
		URI uri = URI.create("/api/orders/" + order.getId());
		return ResponseEntity.created(uri).body(order);
	}

	@GetMapping("/api/orders")
	public ResponseEntity<List<OrderDto>> list() {
		return ResponseEntity.ok().body(orderService.list());
	}

	@PutMapping("/api/orders/{orderId}/order-status")
	public ResponseEntity<OrderDto> changeOrderStatus(
		@PathVariable Long orderId,
		@RequestBody OrderRequest request
	) {
		return ResponseEntity.ok(orderService.changeOrderStatus(orderId, request));
	}
}
