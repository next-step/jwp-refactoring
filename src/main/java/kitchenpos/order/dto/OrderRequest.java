package kitchenpos.order.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {
    private Long orderTableId;
    private OrderStatus orderStatus;
    @Size(min = 1)
    @NotNull
    private List<OrderLineItemRequest> orderLineItems;

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public OrderRequest(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderRequest() {
    }

    public List<OrderLineItem> createOrderLineItems(List<Menu> menus) {
        validateOrderLineItems(menus);
        return orderLineItems.stream()
                .map(orderLineItem -> {
                    Menu menu = menus.stream()
                            .filter(filterMenu -> filterMenu.getId().equals(orderLineItem.getMenuId()))
                            .findFirst()
                            .orElseThrow(IllegalArgumentException::new);
                    return new OrderLineItem(menu, orderLineItem.getQuantity());
                }).collect(Collectors.toList());
    }

    public List<Long> getMenuIds() {
        if (orderLineItems == null) {
            return Collections.emptyList();
        }
        return orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
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

    private void validateOrderLineItems(List<Menu> menus) {
        if(orderLineItems == null) {
            throw new IllegalArgumentException("주문 요청한 메뉴 항목이 존재하지 않습니다.");
        }
        if (orderLineItems.size() != menus.size()) {
            throw new IllegalArgumentException("주문 요청한 메뉴 중에 존재하지 않는 메뉴가 있습니다.");
        }
    }
}
