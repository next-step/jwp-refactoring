package kitchenpos.order.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderRestController {
    private final OrderService orderService;

    public OrderRestController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponse> create(@RequestBody final OrderRequest orderRequest) {
        final Order created = orderService.create(orderRequest);
        OrderResponse orderResponse = OrderResponse.of(created);
        final URI uri = URI.create("/api/orders/" + orderResponse.getId());
        return ResponseEntity.created(uri)
                .body(orderResponse)
                ;
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> list() {
        List<Order> orders = orderService.list();
        List<OrderResponse> orderResponses = orders.stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(orderResponses)
                ;
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderStatusRequest orderStatusRequest
    ) {
        Order changeOrder = orderService.changeOrderStatus(orderId, orderStatusRequest);
        OrderResponse orderResponse = OrderResponse.of(changeOrder);
        return ResponseEntity.ok(orderResponse);
    }
}
