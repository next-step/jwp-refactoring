package kitchenpos.domain.tableGroup;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderTableInTableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderTableId;

    protected OrderTableInTableGroup() {
    }

    OrderTableInTableGroup(final Long id, final Long orderTableId) {
        this.id = id;
        this.orderTableId = orderTableId;
    }

    public OrderTableInTableGroup(final Long orderTableId) {
        this(null, orderTableId);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
