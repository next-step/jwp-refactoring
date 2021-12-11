package kitchenpos.ui;

import kitchenpos.application.OrderService;
import kitchenpos.domain.order.Orders;
import kitchenpos.dto.OrderDto;

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
    public ResponseEntity<OrderDto> create(@RequestBody final OrderDto order) {
        final Orders created = orderService.create(order);
        final URI uri = URI.create("/api/orders/" + created.getId());

        return ResponseEntity.created(uri).body(OrderDto.of(created));
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderDto>> list() {
        return ResponseEntity.ok().body(orderService.list().stream()
                                                    .map(OrderDto::of)
                                                    .collect(Collectors.toList())
                                        );
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderDto> changeOrderStatus(@PathVariable final Long orderId, @RequestBody final OrderDto order) {
        return ResponseEntity.ok(OrderDto.of(orderService.changeOrderStatus(orderId, order)));
    }
}
