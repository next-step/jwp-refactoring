package kitchenpos.domain.order;

import kitchenpos.ValueObjectId;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderLineItem extends ValueObjectId {
    private Long menuId;

    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return super.getSeq();
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderLineItem that = (OrderLineItem) o;
        return quantity == that.quantity && Objects.equals(menuId, that.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuId, quantity);
    }

    @Override
    public String toString() {
        return "OrderLineItem{" +
                "menuId=" + menuId +
                ", quantity=" + quantity +
                '}';
    }
}
