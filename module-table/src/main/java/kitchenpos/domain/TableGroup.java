package kitchenpos.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;
    @Embedded
    private OrderTables orderTables;

    protected TableGroup() {
    }

    public TableGroup(final Long id, final OrderTables orderTables) {
        this.id = id;
        this.createdDate = LocalDateTime.now();
        this.orderTables = orderTables;
    }

    public static TableGroup of(final List<OrderTable> orderTables, int requestOrderTableSize) {
        final OrderTables tables = OrderTables.of(orderTables);
        tables.notMatchCount(requestOrderTableSize);
        return new TableGroup(null, tables);
    }

    public void updateGroupTableId() {
        orderTables.updateGroupTableIdAndEmpty(id, false);
    }

    public void ungroupTable() {
        orderTables.updateGroupTableIdAndEmpty(null, true);
    }

    public List<Long> getOrderTableIds() {
        return orderTables.getOrderTableIds();
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.get();
    }

    @Override
    public String toString() {
        return "TableGroup{" +
                "id=" + id +
                ", createdDate=" + createdDate +
                ", orderTables=" + orderTables +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableGroup that = (TableGroup) o;
        return Objects.equals(id, that.id) && Objects.equals(createdDate, that.createdDate) && Objects.equals(orderTables, that.orderTables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdDate, orderTables);
    }
}
