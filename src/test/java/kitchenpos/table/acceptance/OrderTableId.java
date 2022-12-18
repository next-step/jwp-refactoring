package kitchenpos.table.acceptance;

import java.util.Objects;

public class OrderTableId {
    private final long orderTableId;

    public OrderTableId(long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public long value() {
        return orderTableId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        OrderTableId that = (OrderTableId)o;
        return orderTableId == that.orderTableId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTableId);
    }
}
