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
    private OrderTables orderTables = new OrderTables();

    protected TableGroup() {
    }

    public TableGroup(List<OrderTable> orderTables) {
        this.orderTables = new OrderTables(orderTables);
    }

    public TableGroup(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void create(final LocalDateTime createdDate) {
        this.orderTables.group(this);
        this.createdDate = createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.elements();
    }

    public void changeOrderTables(final List<OrderTable> orderTables) {
        this.orderTables.validateTablesSize(orderTables.size());

        this.orderTables = new OrderTables(orderTables);
        this.orderTables.validateCanGroup();
    }

    public List<Long> getOrderTableIds() {
        return orderTables.getOrderTableIds();
    }

    public void ungroupOrderTables() {
        orderTables.unGroup();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableGroup that = (TableGroup) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
