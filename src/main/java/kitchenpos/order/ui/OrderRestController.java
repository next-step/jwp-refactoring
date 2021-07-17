package kitchenpos.order.ui;

import kitchenpos.order.application.OrderService;
import kitchenpos.domain.OrderRequest;
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
    public ResponseEntity<OrderRequest> create(@RequestBody final OrderRequest order) {
        final OrderRequest created = orderService.create(order);
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderRequest>> list() {
        return ResponseEntity.ok()
                .body(orderService.list())
                ;
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderRequest> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderRequest order
    ) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, order));
    }

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity handleRuntimeException(IllegalArgumentException illegalArgumentException) {
		return ResponseEntity.badRequest().build();
	}
}
