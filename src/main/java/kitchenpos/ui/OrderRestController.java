package kitchenpos.ui;

import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.dto.request.OrderStatusChangeRequest;
import kitchenpos.dto.response.OrderViewResponse;
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
    public ResponseEntity<Order> create(@RequestBody final Order order) {
        final Order created = orderService.create(order);
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderViewResponse>> list() {
        List<OrderViewResponse> results = orderService.list()
                .stream()
                .map(OrderViewResponse::of)
                .collect(Collectors.toList());


        return ResponseEntity.ok()
                .body(results);
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderViewResponse> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderStatusChangeRequest orderStatusChangeRequest
            ) {
        return ResponseEntity.ok(
                OrderViewResponse.of(
                        orderService.changeOrderStatus(orderId, orderStatusChangeRequest.getOrderStatus())
                )
        );
    }
}
