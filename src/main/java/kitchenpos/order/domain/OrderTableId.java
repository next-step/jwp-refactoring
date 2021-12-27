package kitchenpos.order.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.common.exception.Message;

@Embeddable
public class OrderTableId {

    @Column(name = "order_table_id")
    private Long id;

    public OrderTableId(Long id) {
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException(Message.ORDER_TABLE_IS_NOT_NULL.getMessage());
        }
        this.id = id;
    }

    protected OrderTableId() {
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderTableId that = (OrderTableId) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
