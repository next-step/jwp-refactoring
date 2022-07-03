package kitchenpos.orders.order.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.menus.menu.dto.OrderMenuResponse;
import kitchenpos.orders.order.domain.Order;
import kitchenpos.orders.order.domain.OrderLineItem;

public class OrderRequest {
    private Long orderTableId;
    private String orderStatus;
    private List<OrderLineItemRequest> orderLineItems;

    protected OrderRequest(Long orderTableId, String orderStatus) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
    }

    protected OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    protected OrderRequest() {
    }

    public static OrderRequest of(Long orderTableId, String orderStatus) {
        return new OrderRequest(orderTableId, orderStatus);
    }

    public static OrderRequest of(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        return new OrderRequest(orderTableId, orderLineItems);
    }

    public Order toOrder(List<OrderMenuResponse> orderMenus) {
        return new Order(orderTableId, toOrderLineItems(orderMenus));
    }

    public List<OrderLineItem> toOrderLineItems(List<OrderMenuResponse> orderMenus) {
        return orderLineItems.stream()
                .map(orderLineItem -> {
                    OrderMenuResponse findOrderMenu = findOrderMenuById(orderLineItem.getMenuId(), orderMenus);
                    return new OrderLineItem(orderLineItem.getMenuId(), findOrderMenu.getMenuName(),
                            findOrderMenu.getMenuPrice(), orderLineItem.getQuantity());
                }).collect(Collectors.toList());
    }

    public OrderMenuResponse findOrderMenuById(Long id, List<OrderMenuResponse> orderMenus) {
        return orderMenus.stream()
                .filter(orderMenuResponse -> orderMenuResponse.getMenuId() == id)
                .findAny()
                .orElseThrow(NoSuchElementException::new);
    }

    @JsonIgnore
    public List<Long> getMenuIds() {
        return orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
