package kitchenpos.ui;

import dto.order.OrderRequest;
import dto.order.OrderResponse;
import dto.order.OrderStatusChangeRequest;
import kitchenpos.OrderService;
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
        final OrderResponse created = orderService.create(orderRequest);
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> list() {
        return ResponseEntity.ok()
                .body(orderService.list())
                ;
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderStatusChangeRequest orderStatusChangeRequest
    ) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, orderStatusChangeRequest));
    }
}
