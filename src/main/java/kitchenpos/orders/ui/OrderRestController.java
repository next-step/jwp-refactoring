package kitchenpos.orders.ui;

import kitchenpos.orders.application.OrderService;
import kitchenpos.orders.domain.Order;
import kitchenpos.orders.dto.OrderRequest;
import kitchenpos.orders.dto.OrderResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class OrderRestController {
	private final OrderService orderService;

	public OrderRestController(final OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping("/api/orders")
	public ResponseEntity<OrderResponse> create(@RequestBody final OrderRequest orderRequest) {
		final Order created = orderService.create(orderRequest);
		final URI uri = URI.create("/api/orders/" + created.getId());
		return ResponseEntity.created(uri).body(OrderResponse.of(created));
	}

	@GetMapping("/api/orders")
	public ResponseEntity<List<OrderResponse>> list() {
		return ResponseEntity.ok().body(OrderResponse.ofList(orderService.list()));
	}

	@PutMapping("/api/orders/{orderId}/order-status")
	public ResponseEntity<OrderResponse> changeOrderStatus(@PathVariable final Long orderId, @RequestBody final OrderRequest orderRequest) {
		Order updated = orderService.changeOrderStatus(orderId, orderRequest);
		return ResponseEntity.ok(OrderResponse.of(updated));
	}
}
