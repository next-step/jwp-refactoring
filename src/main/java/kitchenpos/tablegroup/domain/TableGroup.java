package kitchenpos.tablegroup.domain;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;
    private OrderTables orderTables;

    private TableGroup(LocalDateTime createdDate, OrderTables orderTables) {
        this.createdDate = createdDate;
        this.orderTables = orderTables;
        orderTables.group(this);
    }

    public TableGroup() {
    }

    public static TableGroup of(OrderTables orderTables) {
        return new TableGroup(LocalDateTime.now(), orderTables);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.value();
    }
}
