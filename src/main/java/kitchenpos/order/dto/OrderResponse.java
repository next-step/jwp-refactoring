package kitchenpos.order.dto;

import static java.util.stream.Collectors.*;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

public class OrderResponse {
    private Long id;
    private Long orderTableId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    public OrderResponse() {
    }

    public OrderResponse(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime,
        List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse of(Order order, List<Menu> menus) {
        return new OrderResponse(order.getId(), order.getOrderTableId(), order.getOrderStatus(),
            order.getOrderedTime(),
            createOrderLineItemResponses(order.getMenuQuantityPairs(menus)));
    }

    private static List<OrderLineItemResponse> createOrderLineItemResponses(List<MenuQuantityPair> pairs) {
        return pairs.stream()
            .map(menuQuantityPair -> new OrderLineItemResponse(menuQuantityPair.getMenuName(),
                menuQuantityPair.getQuantity()))
            .collect(toList());
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
