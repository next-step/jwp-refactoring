package kitchenpos.order.ui;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.request.OrderRequest;
import kitchenpos.order.domain.response.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/api/orders")
public class OrderRestController {

    private final OrderService orderService;

    public OrderRestController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> create(@RequestBody final Order order) {
        final Order created = orderService.create(order);
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Order>> list() {
        return ResponseEntity.ok().body(orderService.list());
    }

    @PutMapping("/{orderId}/order-status")
    public ResponseEntity<Order> changeOrderStatus(@PathVariable final Long orderId,
        @RequestBody final Order order) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, order));
    }

    @PostMapping("/copy")
    public ResponseEntity<OrderResponse> createCopy(@RequestBody final OrderRequest orderRequest) {
        final OrderResponse created = orderService.createCopy(orderRequest);
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping("/copy")
    public ResponseEntity<List<OrderResponse>> listCopy() {
        return ResponseEntity.ok().body(orderService.listCopy());
    }

    @PutMapping("/{orderId}/order-status/copy")
    public ResponseEntity<OrderResponse> changeOrderStatusCopy(@PathVariable final Long orderId,
        @RequestBody final OrderRequest orderRequest) {
        return ResponseEntity.ok(orderService.changeOrderStatusCopy(orderId, orderRequest));
    }
}
