package kitchenpos.table.domain;

import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.BaseEntity;

@Entity
public class TableGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private OrderTables orderTables;

    protected TableGroup() {
    }


    public TableGroup(List<OrderTable> orderTables) {
        this.orderTables = new OrderTables(orderTables);
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.getOrderTables();
    }

    public TableGroup groupTables() {
        orderTables.getOrderTables()
                .forEach(orderTable -> orderTable.groupTable(this));

        return this;
    }

    public void ungroupTables() {
        orderTables.getOrderTables()
                .forEach(OrderTable::ungroupTable);
    }
}
