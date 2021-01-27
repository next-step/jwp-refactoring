package api.order.ui;

import api.order.application.OrderService;
import api.order.dto.OrderRequest_ChangeStatus;
import api.order.dto.OrderRequest_Create;
import api.order.dto.OrderResponse;
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
	public ResponseEntity<OrderResponse> create(@RequestBody OrderRequest_Create request) {
		OrderResponse created = orderService.create(request);
		final URI uri = URI.create("/api/orders/" + created.getId());
		return ResponseEntity.created(uri).body(created);
	}

	@GetMapping("/api/orders")
	public ResponseEntity<List<OrderResponse>> list() {
		return ResponseEntity.ok().body(orderService.list());
	}

	@PutMapping("/api/orders/{orderId}/order-status")
	public ResponseEntity<OrderResponse> changeOrderStatus(@PathVariable Long orderId,
	                                                       @RequestBody OrderRequest_ChangeStatus request) {
		return ResponseEntity.ok(orderService.changeOrderStatus(orderId, request));
	}
}
