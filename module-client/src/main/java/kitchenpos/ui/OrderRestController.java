package kitchenpos.ui;

import kitchenpos.dto.ChangeStatusRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import kitchenpos.service.OrderService;

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
        OrderResponse order = orderService.create(orderRequest);
        final URI uri = URI.create("/api/orders/" + order.getId());
        return ResponseEntity.created(uri)
                .body(order);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> list() {
        return ResponseEntity.ok()
                .body(orderService.findAll());
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final ChangeStatusRequest request) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, request.getOrderStatus()));
    }
}
