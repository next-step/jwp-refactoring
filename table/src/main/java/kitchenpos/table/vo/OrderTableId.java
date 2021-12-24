package kitchenpos.table.vo;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import kitchenpos.table.domain.OrderTable;

@Embeddable
public class OrderTableId {
    @Column(name = "order_table_id")
    private final Long id;

    protected OrderTableId() {
        this.id = null;
    }

    private OrderTableId(Long id) {
        this.id = id;
    }

    public static OrderTableId of(Long id) {
        return new OrderTableId(id);
    }
    public static OrderTableId of(OrderTable menu) {
        return new OrderTableId(menu.getId());
    }

    public Long value() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof OrderTableId)) {
            return false;
        }
        OrderTableId orderTableId = (OrderTableId) o;
        return Objects.equals(this.id, orderTableId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}