package kitchenpos.tablegroup.domain;

import kitchenpos.ordertable.domain.OrderTable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables = new OrderTables();

    protected TableGroup() {
    }

    public TableGroup(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public static TableGroup of(LocalDateTime createdDate, List<OrderTable> orderTables) {
        validate(orderTables);
        TableGroup tableGroup = new TableGroup(createdDate);
        orderTables.forEach(it -> it.changeTableGroup(tableGroup));
        return tableGroup;
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

    public void ungroup() {
        orderTables.ungroup();
    }

    public void addToOrderTables(OrderTable orderTable) {
        orderTables.add(orderTable);
    }

    private static void validate(List<OrderTable> orderTables) {
        orderTables.forEach(TableGroup::validateOrderTable);
    }

    private static void validateOrderTable(OrderTable orderTable) {
        if (!orderTable.isGroupable()) {
            throw new IllegalArgumentException();
        }
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
