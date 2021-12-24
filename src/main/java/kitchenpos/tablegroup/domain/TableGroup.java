package kitchenpos.tablegroup.domain;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableValidator;
import kitchenpos.order.domain.OrderTables;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate = LocalDateTime.now();
    @Embedded
    private OrderTables orderTables = new OrderTables();

    public TableGroup() {
    }

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = new OrderTables(orderTables);
    }

    public TableGroup(List<OrderTable> orderTables, int orderTablesSize) {
        this.orderTables = OrderTables.of(this, orderTables, orderTablesSize);
    }

    public void ungroup(OrderTableValidator orderTableValidator) {
        orderTables.ungroup(orderTableValidator);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.values();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableGroup that = (TableGroup) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
