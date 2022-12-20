package kitchenpos.order.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.product.domain.MenuProducts;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderResponse {

    private Long id;
    private OrderTableResponse orderTableResponse;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    public OrderResponse(Long id, OrderTableResponse orderTableResponse, String orderStatus, LocalDateTime orderedTime,
                         List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTableResponse = orderTableResponse;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse of(Order order, List<OrderLineItem> orderLineItems, Map<Long, Menu> menus,
                                   OrderTable orderTable, Map<Long, MenuProducts> menuProducts) {
        List<OrderLineItemResponse> orderLineItemResponses = orderLineItems
                .stream()
                .map(orderLineItem -> OrderLineItemResponse.of(orderLineItem,
                        menus.get(orderLineItem.getMenuId()), menuProducts.get(orderLineItem.getMenuId())))
                .collect(Collectors.toList());

        return new OrderResponse(order.getId(),
                OrderTableResponse.of(orderTable),
                order.getOrderStatus().name(),
                order.getOrderedTime(),
                orderLineItemResponses);
    }

    public Long getId() {
        return id;
    }

    public OrderTableResponse getOrderTableResponse() {
        return orderTableResponse;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
