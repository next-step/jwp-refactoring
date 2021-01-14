package kitchenpos.domain.tablegroup;

import kitchenpos.ValueObjectId;

import javax.persistence.Entity;
import java.util.Objects;

@Entity
public class OrderTableInTableGroup extends ValueObjectId {
    private Long orderTableId;

    protected OrderTableInTableGroup() {
    }

    public OrderTableInTableGroup(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderTableInTableGroup that = (OrderTableInTableGroup) o;
        return Objects.equals(orderTableId, that.orderTableId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTableId);
    }

    @Override
    public String toString() {
        return "OrderTableInTableGroup{" +
                "orderTableId=" + orderTableId +
                '}';
    }
}
