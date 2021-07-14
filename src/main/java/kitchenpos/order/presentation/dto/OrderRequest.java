package kitchenpos.order.presentation.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.presentation.dto.exception.BadMenuIdException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderRequest {
    private Long orderTableId;
    private OrderStatus orderStatus;
    private List<OrderLineItemRequest> orderLineItems;

    protected OrderRequest() {
    }

    private OrderRequest(Long orderTableId, OrderStatus orderStatus, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public static OrderRequest of(Long orderTableId, OrderStatus orderStatus, List<OrderLineItemRequest> orderLineItems) {
        return new OrderRequest(orderTableId, orderStatus, orderLineItems);
    }

    public List<Long> getMenuIds() {
        return orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }

    public List<OrderLineItem> getOrderLineItemsBy(List<Menu> menus) {
        validateCount(menus);
        return menus.stream()
                .map(this::createOrderLineItemWith)
                .collect(Collectors.toList());
    }

    private void validateCount(List<Menu> menus) {
        if (orderLineItems.size() != menus.size()) {
            throw new BadMenuIdException();
        }
    }

    private OrderLineItem createOrderLineItemWith(Menu menu) {
        return orderLineItems.stream()
                .filter(orderLineItemRequest -> isMenuIdMatch(orderLineItemRequest, menu))
                .map(orderLineItemRequest -> OrderLineItem.of(menu, orderLineItemRequest.getQuantity()))
                .findFirst()
                .orElseThrow(BadMenuIdException::new);
    }

    private boolean isMenuIdMatch(OrderLineItemRequest orderLineItemRequest, Menu menu) {
        return Objects.equals(orderLineItemRequest.getMenuId(), menu.getId());
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
