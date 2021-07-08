package kitchenpos.ui.api;

import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.ui.dto.order.OrderRequest;
import kitchenpos.ui.dto.order.OrderResponse;
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
    public ResponseEntity<Order> create(@RequestBody final Order order) {
        final Order created = orderService.create(order);
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<Order>> list() {
        return ResponseEntity.ok()
                .body(orderService.list())
                ;
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<Order> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final Order order
    ) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, order));
    }

    @PostMapping("/api/orders2")
    public ResponseEntity<OrderResponse> create2(@RequestBody final OrderRequest request) {
        final Order created = orderService.create(request.toOrder());
        final URI uri = URI.create("/api/orders2/" + created.getId());
        return ResponseEntity.created(uri)
                .body(OrderResponse.of(created))
                ;
    }

    @GetMapping("/api/orders2")
    public ResponseEntity<List<OrderResponse>> list2() {
        return ResponseEntity.ok()
                .body(OrderResponse.ofList(orderService.list()))
                ;
    }

    @PutMapping("/api/orders2/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus2(
            @PathVariable final Long orderId,
            @RequestBody final OrderRequest request
    ) {
        return ResponseEntity.ok(OrderResponse.of(orderService.changeOrderStatus(orderId, request.toOrder())));
    }
}
