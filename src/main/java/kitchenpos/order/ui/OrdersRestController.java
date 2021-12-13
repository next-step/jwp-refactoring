package kitchenpos.order.ui;

import kitchenpos.order.application.OrdersService;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.dto.OrdersResponse;
import kitchenpos.order.dto.OrdersRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class OrdersRestController {
    private final OrdersService ordersService;

    public OrdersRestController(final OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<OrdersResponse> create(@RequestBody final OrdersRequest ordersRequest) {
        final OrdersResponse created = ordersService.create(ordersRequest.toOrders());
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrdersResponse>> list() {
        return ResponseEntity.ok()
                .body(ordersService.list())
                ;
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrdersResponse> changeOrderStatus(@PathVariable final Long orderId,
                                                    @RequestBody final OrdersRequest ordersRequest) {
        return ResponseEntity.ok(ordersService.changeOrderStatus(orderId, ordersRequest.toOrders()));
    }
}
