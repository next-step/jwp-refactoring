package kitchenpos.order.ui;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusChangeRequest;
import kitchenpos.order.exception.EmptyOrderTableException;
import kitchenpos.order.exception.OrderStatusCompleteException;
import kitchenpos.orderlineitem.exception.EmptyOrderLineItemsException;
import kitchenpos.orderlineitem.exception.MenuAndOrderLineItemSizeNotMatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class OrderRestController {
    private final OrderService orderService;

    public OrderRestController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity create(@RequestBody OrderCreateRequest orderCreateRequest) {
        final OrderResponse createdOrderResponse = orderService.create(orderCreateRequest);
        final URI uri = URI.create("/api/orders/" + createdOrderResponse.getId());
        return ResponseEntity.created(uri)
                .body(createdOrderResponse);
    }

    @GetMapping("/api/orders")
    public ResponseEntity list() {
        return ResponseEntity.ok()
                .body(orderService.list());
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody OrderStatusChangeRequest orderStatusChangeRequest
    ) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, orderStatusChangeRequest));
    }

    @ExceptionHandler({
            EmptyOrderLineItemsException.class, MenuAndOrderLineItemSizeNotMatchException.class,
            EmptyOrderTableException.class, OrderStatusCompleteException.class
    })
    public ResponseEntity handleException(RuntimeException e) {
        return new ResponseEntity(e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
