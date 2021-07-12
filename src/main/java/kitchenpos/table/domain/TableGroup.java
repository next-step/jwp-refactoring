package kitchenpos.table.domain;

import kitchenpos.BaseEntity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class TableGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private OrderTables orderTables;

    public TableGroup() { }

    public TableGroup(List<OrderTable> orderTables) {
        this.orderTables = new OrderTables(orderTables);
    }

    public TableGroup(Long id, List<OrderTable> orderTables) {
        this.id = id;
        this.orderTables = new OrderTables(orderTables);
    }

    public static TableGroup of(List<OrderTable> savedOrderTables) {
        return new TableGroup(savedOrderTables);
    }

    public List<Long> getOrderTableIds() {
        return orderTables.getOrderTableIds();
    }

    public void grouping() {
        orderTables.groupingWith(this);
    }

    public void ungrouping() {
        orderTables.ungrouping();
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableGroup that = (TableGroup) o;
        return Objects.equals(id, that.id) && Objects.equals(orderTables, that.orderTables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderTables);
    }

}
