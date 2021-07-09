package kitchenpos.tablegroup.domain;

import kitchenpos.ordertable.domain.OrderTable;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
public class TableGroup {
    private static final int TABLE_GROUP_MIN = 2;
    static final String MSG_TABLE_COUNT_LEAST = String.format("TableIds'size must be at least %d", TABLE_GROUP_MIN);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    public static TableGroup fromGroupingTables(List<OrderTable> orderTables) {
        return new TableGroup(orderTables);
    }

    public TableGroup() {
    }

    public TableGroup(List<OrderTable> orderTables) {
        groupTables(orderTables);
    }

    private void groupTables(List<OrderTable> orderTables) {
        if (orderTables.size() < TABLE_GROUP_MIN) {
            throw new IllegalArgumentException(MSG_TABLE_COUNT_LEAST);
        }

        orderTables.forEach(orderTable -> orderTable.putIntoGroup(this));
        this.orderTables = orderTables;
    }

    public Long getId() {
        return id;
    }

    public void ungroupTables() {
        orderTables.forEach(OrderTable::ungroup);
        this.orderTables = Collections.emptyList();
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TableGroup)) return false;
        if (!super.equals(o)) return false;
        TableGroup that = (TableGroup) o;
        return Objects.equals(createdDate, that.createdDate) &&
                Objects.equals(orderTables, that.orderTables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), createdDate, orderTables);
    }
}