package kitchenpos.tablegroup.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;
    @Embedded
    private OrderTables orderTables = new OrderTables();

    protected TableGroup() {
        this.createdDate = LocalDateTime.now();
    }

    public static TableGroup empty() {
        return new TableGroup();
    }

    public static TableGroup group(OrderTables orderTables) {
        TableGroup tableGroup = TableGroup.empty();
        for (OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || orderTable.hasTableGroup()) {
                throw new IllegalArgumentException();
            }
            orderTable.group(tableGroup);
            tableGroup.add(orderTable);
        }
        return tableGroup;
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.getAll();
    }

    public void add(OrderTable orderTable) {
        this.orderTables.add(orderTable);
    }

    public void unGroup() {
        for (final OrderTable orderTable : this.orderTables) {
            orderTable.unGroup();
        }
    }
}
