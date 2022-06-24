package kitchenpos.ui;

import kitchenpos.application.OrderService;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.OrderRequestDto;
import kitchenpos.dto.OrderResponseDto;
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
    public ResponseEntity<OrderResponseDto> create(@RequestBody final OrderRequestDto request) {
        final OrderResponseDto created = orderService.create(request);
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponseDto>> list() {
        return ResponseEntity.ok()
                .body(orderService.list())
                ;
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponseDto> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderStatus orderStatus
    ) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, orderStatus));
    }
}
