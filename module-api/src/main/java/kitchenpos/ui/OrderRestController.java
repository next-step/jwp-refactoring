package kitchenpos.ui;

import kitchenpos.application.command.OrderQueryService;
import kitchenpos.application.command.OrderService;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderStatusChangeRequest;
import kitchenpos.dto.response.OrderViewResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class OrderRestController {
    private final OrderService orderService;
    private final OrderQueryService orderQueryService;

    public OrderRestController(OrderService orderService, OrderQueryService orderQueryService) {
        this.orderService = orderService;
        this.orderQueryService = orderQueryService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<OrderViewResponse> create(@RequestBody final OrderCreateRequest orderCreateRequest) {

        final Long id = orderService.create(orderCreateRequest.toCreate());

        return ResponseEntity.created(URI.create("/api/orders/" + id))
                .body(orderQueryService.findById(id));
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderViewResponse>> list() {
        return ResponseEntity.ok()
                .body(orderQueryService.list());
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderViewResponse> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderStatusChangeRequest orderStatusChangeRequest
            ) {
        orderService.changeOrderStatus(orderId, orderStatusChangeRequest.getOrderStatus());

        return ResponseEntity.ok(orderQueryService.findById(orderId));
    }
}
