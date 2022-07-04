package kitchenpos.domain.table;

import java.util.List;
import javax.persistence.*;
import kitchenpos.domain.common.BaseEntity;

@Entity
public class TableGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private OrderTables orderTables = new OrderTables();

    public TableGroup() {}

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.getOrderTables();
    }

    public void addOrderTables(OrderTables orderTables) {
        this.orderTables = orderTables;
    }
}
