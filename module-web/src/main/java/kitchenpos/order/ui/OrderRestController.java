package kitchenpos.order.ui;

import java.net.*;
import java.util.*;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import kitchenpos.order.application.*;
import kitchenpos.order.dto.*;
import kitchenpos.table.dto.*;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {
    private final OrderService orderService;

    public OrderRestController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody final OrderRequest orderRequest) {
        final OrderResponse created = orderService.saveOrder(orderRequest);
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> findAllOrder() {
        return ResponseEntity.ok()
            .body(orderService.findAll());
    }

    @PutMapping("/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(@PathVariable final Long orderId, @RequestBody final OrderRequest orderRequest) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, orderRequest));
    }

    @PutMapping("/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(@PathVariable final Long orderTableId) {
        return ResponseEntity.ok()
            .body(orderService.cleanTable(orderTableId));
    }
}
