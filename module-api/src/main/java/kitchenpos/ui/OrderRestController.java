package kitchenpos.ui;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.dto.ChangeOrderStatusRequest;
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
    public ResponseEntity<OrderResponse> create(@RequestBody final OrderRequest request) {
        final OrderResponse response = orderService.create(request);
        final URI uri = URI.create("/api/orders/" + response.getOrderId());
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> list() {
        return ResponseEntity.ok(orderService.list());
    }

    @PutMapping("/{orderId}/order-status")
    public ResponseEntity<Void> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final ChangeOrderStatusRequest request
    ) {
        orderService.changeOrderStatus(orderId, request);
        return ResponseEntity.ok().build();
    }
}
