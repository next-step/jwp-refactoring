package kitchenpos.api;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {
	private final OrderService orderService;

	public OrderRestController(final OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping
	public ResponseEntity<OrderResponse> create(@RequestBody final OrderRequest orderRequest) {
		final OrderResponse created = orderService.create(orderRequest);
		final URI uri = URI.create("/api/orders/" + created.getOrderId());
		return ResponseEntity.created(uri).body(created);
	}

	@GetMapping
	public ResponseEntity<List<OrderResponse>> list() {
		return ResponseEntity.ok().body(orderService.list());
	}

	@PutMapping("/{orderId}/order-status")
	public ResponseEntity<OrderResponse> changeOrderStatus(
			@PathVariable final Long orderId,
			@RequestBody final String orderStatus
	) {
		return ResponseEntity.ok(orderService.changeOrderStatus(orderId, orderStatus));
	}
}
