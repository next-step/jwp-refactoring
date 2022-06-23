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

    public TableGroup() {
        this.createdDate = LocalDateTime.now();
    }

    public TableGroup(List<OrderTable> savedOrderTables) {
        for(OrderTable orderTable: savedOrderTables) {
            add(orderTable);
        }
        this.createdDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.getAll();
    }

    public void add(OrderTable orderTable) {
        this.orderTables.add(orderTable);
        orderTable.updateTableGroup(this);
    }

    public void unGroup() {
        this.orderTables.unGroup();
    }
}
