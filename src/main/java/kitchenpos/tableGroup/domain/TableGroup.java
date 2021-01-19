package kitchenpos.tableGroup.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private OrderTables orderTables = new OrderTables();

    private LocalDateTime createdDate;

    public TableGroup() {
    }

    public TableGroup(OrderTables orderTables) {
        this.orderTables = orderTables;
        this.createdDate = LocalDateTime.now();
    }

    public void removeTable(OrderTable orderTable) {
        orderTables.removeTable(orderTable);
    }

    public void addTable(OrderTable orderTable) {
        orderTables.addTable(orderTable);
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

    public boolean hasContain(OrderTable orderTable) {
        return orderTables.hasContain(orderTable);
    }
}
