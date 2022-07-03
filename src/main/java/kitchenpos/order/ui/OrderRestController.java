package kitchenpos.order.ui;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.ui.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrderRestController {
    private final OrderService orderService;

    public OrderRestController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<OrderCreateResponse> create(@RequestBody final OrderCreateRequest request) {
        LocalDateTime orderedTime = LocalDateTime.now();
        final OrderCreateResponse created = new OrderCreateResponse(orderService.create(request.toEntity(orderedTime)));
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> list() {
        return ResponseEntity.ok()
                .body(orderService.list()
                        .stream()
                        .map(OrderResponse::new)
                        .collect(Collectors.toList())
                )
                ;
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderUpdateResponse> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderUpdateRequest request
    ) {
        OrderUpdateResponse response = new OrderUpdateResponse(orderService.changeOrderStatus(orderId, request.toEntity()));
        return ResponseEntity.ok(response);
    }
}
