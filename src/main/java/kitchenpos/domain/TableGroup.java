package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables = new OrderTables();

    protected TableGroup() {
    }

    public TableGroup(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public TableGroup(Long id) {
        this.id = id;
    }

    public TableGroup(Long id, LocalDateTime createdDate, OrderTables orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
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

    public List<OrderTable> getOrderTableValues() {
        return orderTables.getOrderTables();
    }

    private void addOrderTable(OrderTable orderTable) {
        this.orderTables.add(orderTable);
        orderTable.updateEmpty(false);
        orderTable.referenceTableGroup(this);
    }

    public void addOrderTables(OrderTables orderTables) {
        for (final OrderTable orderTable : orderTables.getOrderTables()) {
            addOrderTable(orderTable);
        }
    }
}
