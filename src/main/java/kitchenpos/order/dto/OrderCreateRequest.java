package kitchenpos.order.dto;

import kitchenpos.menu.domain.Menus;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.table.domain.OrderTable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderCreateRequest {
    private Long orderTable;
    private List<OrderLineItemRequest> orderLineItems = new ArrayList<>();

    protected OrderCreateRequest() {}

    public OrderCreateRequest(Long orderTable, List<OrderLineItemRequest> orderLineItems) {
        this.orderTable = orderTable;
        this.orderLineItems.addAll(orderLineItems);
    }

    public Order of(OrderTable orderTable, OrderLineItems orderLineItems) {
        return new Order(orderTable, orderLineItems);
    }

    public Long getOrderTable() {
        return orderTable;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public List<Long> getMenus() {
        return orderLineItems.stream()
                .map(OrderLineItemRequest::getMenu)
                .collect(Collectors.toList());
    }

    public void checkAllMenuIsExist(final Menus menus) {
        if (menus.isNotAllContainIds(this.getMenus())) {
            throw new IllegalArgumentException("주문에 저장되지 않은 메뉴가 존재합니다.");
        }
    }
}
