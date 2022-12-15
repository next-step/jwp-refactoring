package kitchenpos.order.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

import java.util.List;

public class OrderLineItemRequest {
    private Long menuId;
    private long quantity;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public OrderLineItem toOrderLineItem(Order order, List<Menu> menus) {
        return OrderLineItem.builder()
                .order(order)
                .quantity(quantity)
                .menu(menus.stream().filter(menu -> menu.getId().equals(menuId)).findFirst().get())
                .build();
    }
}
