package kitchenpos.ui;

import kitchenpos.application.OrderService;
import kitchenpos.domain.Quantity;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderCreate;
import kitchenpos.domain.order.OrderLineItemCreate;
import kitchenpos.dto.request.OrderCreateRequest;
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
    public ResponseEntity<OrderViewResponse> create(@RequestBody final OrderCreateRequest orderCreateRequest) {
        List<OrderLineItemCreate> itemCreates = orderCreateRequest.getOrderLineItems()
                .stream()
                .map(item -> new OrderLineItemCreate(item.getMenuId(), new Quantity(item.getQuantity())))
                .collect(Collectors.toList());

        final Order created = orderService.create(
                new OrderCreate(
                        orderCreateRequest.getOrderTableId(),
                        orderCreateRequest.getOrderStatus(),
                        itemCreates
                )
        );
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri)
                .body(OrderViewResponse.of(created));
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
