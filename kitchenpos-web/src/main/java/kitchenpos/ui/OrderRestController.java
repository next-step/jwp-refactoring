package kitchenpos.ui;

import kitchenpos.dto.OrderDto;
import kitchenpos.application.OrderService;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderStatusChangeDto;
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
    public ResponseEntity<OrderDto> create(@RequestBody final OrderCreateRequest order) {
        final OrderDto created = orderService.create(order);
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderDto>> list() {
        return ResponseEntity.ok()
                .body(orderService.list())
                ;
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderDto> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderStatusChangeDto orderChangeDto
    ) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, orderChangeDto.getOrderStatus()));
    }
}
