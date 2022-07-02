package kitchenpos.order.ui;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
public class OrderRestController {
    private final OrderService orderService;

    public OrderRestController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponse> create(@RequestBody final OrderRequest request) {
        final Order created = orderService.create(request.toEntity());
        final URI uri = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(uri)
                .body(new OrderResponse(created))
                ;
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> list() {
        return ResponseEntity.ok()
                .body(orderService.list().stream()
                        .map(OrderResponse::new)
                        .collect(toList()))
                ;
    }

    @PutMapping("/api/orders/{orderId}/order-status")
    public ResponseEntity<OrderResponse> changeOrderStatus(
            @PathVariable final Long orderId,
            @RequestBody final OrderStatusRequest request
    ) {
        Order changedOrder = orderService.changeOrderStatus(orderId, request.getOrderStatus());
        return ResponseEntity.ok(new OrderResponse(changedOrder));
    }

    static class OrderStatusRequest {
        private OrderStatus orderStatus;

        public OrderStatus getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
        }
    }

    static class OrderRequest {
        private Long orderTableId;
        private List<OrderLineItemRequest> orderLineItems;

        public Long getOrderTableId() {
            return orderTableId;
        }

        public void setOrderTableId(Long orderTableId) {
            this.orderTableId = orderTableId;
        }

        public List<OrderLineItemRequest> getOrderLineItems() {
            return orderLineItems;
        }

        public void setOrderLineItems(List<OrderLineItemRequest> orderLineItems) {
            this.orderLineItems = orderLineItems;
        }

        public Order toEntity() {
            Order order = new Order(orderTableId);
            orderLineItems.stream()
                    .map(OrderLineItemRequest::toEntity)
                    .forEach(order::addOrderLineItem);
            return order;
        }
    }

    static class OrderLineItemRequest {
        private Long menuId;
        private long quantity;

        public Long getMenuId() {
            return menuId;
        }

        public void setMenuId(Long menuId) {
            this.menuId = menuId;
        }

        public long getQuantity() {
            return quantity;
        }

        public void setQuantity(long quantity) {
            this.quantity = quantity;
        }

        public OrderLineItem toEntity() {
            return new OrderLineItem(menuId, quantity);
        }
    }

    static class OrderResponse {
        private Long id;
        private Long orderTableId;
        private OrderStatus orderStatus;
        private LocalDateTime orderedTime;
        private List<OrderLineItemResponse> orderLineItems;

        public OrderResponse(Order order) {
            id = order.getId();
            orderTableId = order.getOrderTableId();
            orderStatus = order.getOrderStatus();
            orderedTime = order.getOrderedTime();
            orderLineItems = order.getOrderLineItems().getOrderLineItems().stream()
                    .map(OrderLineItemResponse::new)
                    .collect(toList());
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getOrderTableId() {
            return orderTableId;
        }

        public void setOrderTableId(Long orderTableId) {
            this.orderTableId = orderTableId;
        }

        public OrderStatus getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
        }

        public LocalDateTime getOrderedTime() {
            return orderedTime;
        }

        public void setOrderedTime(LocalDateTime orderedTime) {
            this.orderedTime = orderedTime;
        }

        public List<OrderLineItemResponse> getOrderLineItems() {
            return orderLineItems;
        }

        public void setOrderLineItems(List<OrderLineItemResponse> orderLineItems) {
            this.orderLineItems = orderLineItems;
        }
    }

    static class OrderLineItemResponse {
        private Long seq;
        private Long menuId;
        private long quantity;

        public OrderLineItemResponse(OrderLineItem orderLineItem) {
            seq = orderLineItem.getSeq();
            menuId = orderLineItem.getMenuId();
            quantity = orderLineItem.getQuantity();
        }

        public Long getSeq() {
            return seq;
        }

        public void setSeq(Long seq) {
            this.seq = seq;
        }

        public Long getMenuId() {
            return menuId;
        }

        public void setMenuId(Long menuId) {
            this.menuId = menuId;
        }

        public long getQuantity() {
            return quantity;
        }

        public void setQuantity(long quantity) {
            this.quantity = quantity;
        }
    }
}
