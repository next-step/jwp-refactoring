package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;

import java.util.List;
import java.util.Optional;

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

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public Optional<OrderLineItem> toEntity(List<OrderMenu> orderMenus) {
        return orderMenus.stream()
                .filter(orderMenu -> orderMenu.getMenuId().equals(menuId))
                .findFirst()
                .map(this::toEntity);
    }

    private OrderLineItem toEntity(OrderMenu orderMenu) {
        return new OrderLineItem(orderMenu, quantity);
    }
}
