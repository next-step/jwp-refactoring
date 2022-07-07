package kitchenpos.order.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.exception.OrderException;
import kitchenpos.order.exception.OrderExceptionType;

import java.util.List;
import java.util.Objects;

public class OrderLineItemRequest {
    private final Long menuId;
    private final long quantity;

    public OrderLineItemRequest(final Long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public OrderLineItem toEntity(final List<Menu> menus) {
        final Menu menu = menus.stream()
                .filter(it -> Objects.equals(it.getId(), menuId))
                .findFirst()
                .orElseThrow(() -> new OrderException(OrderExceptionType.NOT_MATCH_MENU_SIZE));

        return OrderLineItem.of(menu, quantity);
    }

    @Override
    public String toString() {
        return "OrderLineItemRequest{" +
                "menuId=" + menuId +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderLineItemRequest that = (OrderLineItemRequest) o;
        return quantity == that.quantity && Objects.equals(menuId, that.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuId, quantity);
    }
}
