package kitchenpos.order.ui;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Orders;

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
    public ResponseEntity<Orders> create(@RequestBody final Orders orders) {
        final Orders created = orderService.create(orders);
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<Orders>> list() {
        return ResponseEntity.ok()
                .body(orderService.list())
                ;
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<Orders> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final Orders orders
    ) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, orders));
    }
}
