package kitchenpos.vo;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import kitchenpos.domain.table.OrderTable;

@Embeddable
public class OrderTableId {
    @Column(name = "order_table_id")
    private final Long orderTableId;

    protected OrderTableId() {
        this.orderTableId = null;
    }

    private OrderTableId(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public static OrderTableId of(Long orderTableId) {
        return new OrderTableId(orderTableId);
    }
    public static OrderTableId of(OrderTable menu) {
        return new OrderTableId(menu.getId());
    }

    public Long value() {
        return this.orderTableId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof OrderTableId)) {
            return false;
        }
        OrderTableId orderTableId = (OrderTableId) o;
        return Objects.equals(this.orderTableId, orderTableId.orderTableId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(orderTableId);
    }
}