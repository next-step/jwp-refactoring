package kitchenpos.order.ui;

import io.swagger.annotations.ApiOperation;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
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

    @ApiOperation("주문 생성")
    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponse> create(@RequestBody final OrderRequest order) {
        final OrderResponse created = orderService.create(order);
        return ResponseEntity.created(URI.create("/api/orders/" + created.getId())).body(created);
    }

    @ApiOperation("전체 주문 조회")
    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> list() {
        return ResponseEntity.ok().body(orderService.list());
    }

    @ApiOperation("주문 상태 변경")
    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(@PathVariable final Long orderId,
                                                           @RequestBody final OrderStatusRequest orderStatusRequest) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, orderStatusRequest));
    }
}
