package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderMenu;

import java.util.Objects;

public class OrderMenuResponse {
    private final Long id;
    private final Long menuId;
    private final Long quantity;

    public static OrderMenuResponse from(OrderMenu orderMenu) {
        return new OrderMenuResponse(orderMenu.getId(), orderMenu.getMenu().getId(), orderMenu.getQuantity());
    }

    private OrderMenuResponse(Long id, Long menuId, Long quantity) {
        this.id = id;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderMenuResponse that = (OrderMenuResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(menuId, that.menuId) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, menuId, quantity);
    }

    @Override
    public String toString() {
        return "OrderMenuResponse{" +
                "seq=" + id +
                ", menuId=" + menuId +
                ", quantity=" + quantity +
                '}';
    }
}
