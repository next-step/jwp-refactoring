package kitchenpos.order.ui;

import kitchenpos.order.application.OrderCrudService;
import kitchenpos.order.application.OrderStatusService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class OrderRestController {
    private final OrderCrudService orderCrudService;
    private final OrderStatusService orderStatusService;

    public OrderRestController(final OrderCrudService orderCrudService, final OrderStatusService orderStatusService) {
        this.orderCrudService = orderCrudService;
        this.orderStatusService = orderStatusService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponse> create(@RequestBody final OrderCreateRequest request) {
        final OrderResponse created = orderCrudService.create(request);
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<Order>> list() {
        return ResponseEntity.ok()
                .body(orderCrudService.list());
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<Order> changeOrderStatus(@PathVariable final Long orderId, @RequestBody final Order order) {
        return ResponseEntity.ok(orderStatusService.changeOrderStatus(orderId, order));
    }
}
