package kitchenpos.tablegroup;

import kitchenpos.BaseEntity;
import kitchenpos.table.OrderTable;
import kitchenpos.table.OrderTables;

import javax.persistence.*;
import java.util.List;

@Entity
public class TableGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private final OrderTables orderTables = OrderTables.createInstance();

    protected TableGroup() {
    }

    public static TableGroup createInstance() {
        return new TableGroup();
    }

    public void groupingOrderTable(final List<OrderTable> orderTables) {
        this.orderTables.add(orderTables);
        this.orderTables.linkTableGroupId(this.id);
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return this.orderTables.getOrderTables();
    }

    public void ungroup() {
        orderTables.ungroup();
    }
}
