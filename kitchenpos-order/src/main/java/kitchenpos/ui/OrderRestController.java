package kitchenpos.ui;

import kitchenpos.application.OrderService;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
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
    public ResponseEntity<OrderResponse> create(@RequestBody final OrderRequest orderRequest) {
        final Long createdOrderId = orderService.create(orderRequest);
        final OrderResponse createdOrder = orderService.convertOrderResponse(createdOrderId);
        final URI uri = URI.create("/api/orders/" + createdOrder.getId());
        return ResponseEntity.created(uri)
                .body(createdOrder)
                ;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> list() {
        return ResponseEntity.ok()
                .body(orderService.list())
                ;
    }

    @PutMapping(name = "/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderRequest orderRequest
    ) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, orderRequest));
    }
}
