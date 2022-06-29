package kitchenpos.ui;

import kitchenpos.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrderRestController {
    private final OrderService orderService;

    public OrderRestController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponse> create(@RequestBody final Order order) {
        final OrderResponse created = OrderResponse.of(orderService.create(order));
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> list() {
        List<OrderResponse> list = orderService.list().stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(list);
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(@PathVariable final Long orderId, @RequestBody final Order order) {
        Order changeOrder = orderService.changeOrderStatus(orderId, order);
        return ResponseEntity.ok(OrderResponse.of(changeOrder));
    }
}
