package kitchenpos.order.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.dto.OrderChangeStatusRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private static final String BASE_PATH = "/api/orders/";

    private final OrderService orderService;

    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(BASE_PATH)
    public ResponseEntity<OrderResponse> register(@RequestBody final OrderRequest request) {
        final OrderResponse response = orderService.place(request);
        final URI uri = URI.create(BASE_PATH + response.getId());
        return ResponseEntity.created(uri)
            .body(response);
    }

    @GetMapping(BASE_PATH)
    public ResponseEntity<List<OrderResponse>> list() {
        return ResponseEntity.ok()
            .body(orderService.list());
    }

    @PutMapping("{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(
        @PathVariable final Long orderId,
        @RequestBody final OrderChangeStatusRequest request
    ) {
        return ResponseEntity.ok(orderService.changeStatus(orderId, request));
    }
}
