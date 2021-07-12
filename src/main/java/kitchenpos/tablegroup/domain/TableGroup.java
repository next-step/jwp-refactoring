package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import kitchenpos.table.domain.OrderTable;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;
    @Embedded
    private OrderTables orderTables;

    public TableGroup() {
        this.createdDate = LocalDateTime.now();
        this.orderTables = new OrderTables();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public OrderTables getOrderTables() {
        return orderTables;
    }

    public void addOrderTable(OrderTable orderTable) {
        this.orderTables.add(orderTable);
        orderTable.setTableGroup(this);
    }
}
