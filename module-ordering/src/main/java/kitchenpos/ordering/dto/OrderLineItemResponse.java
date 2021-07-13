package kitchenpos.ordering.dto;

import kitchenpos.ordering.domain.OrderLineItem;

import java.time.LocalDateTime;
import java.util.Objects;

public class OrderLineItemResponse {
    private Long id;
    private Long menuId;
    private long quantity;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public OrderLineItemResponse() {
    }

    public OrderLineItemResponse(Long id, Long menuId, long quantity, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.menuId = menuId;
        this.quantity = quantity;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static OrderLineItemResponse of(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getId(),
                orderLineItem.getMenuId(),
                orderLineItem.getQuantity(),
                orderLineItem.getCreatedDate(),
                orderLineItem.getModifiedDate());
    }

    public Long getId() {
        return id;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderLineItemResponse that = (OrderLineItemResponse) o;
        return quantity == that.quantity && Objects.equals(id, that.id) && Objects.equals(menuId, that.menuId) && Objects.equals(createdDate, that.createdDate) && Objects.equals(modifiedDate, that.modifiedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, menuId, quantity, createdDate, modifiedDate);
    }
}
