package kitchenpos.table.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.exception.InvalidArgumentException;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Embedded
    private OrderTables orderTables = new OrderTables();

    public TableGroup() {
    }

    public static TableGroup create() {
        return new TableGroup();
    }

    public void addOrderTables(List<OrderTable> orderTables) {
        this.orderTables.validateAddTables(orderTables);

        for (OrderTable orderTable: orderTables) {
            orderTable.updateEmpty(Boolean.FALSE);
            addOrderTable(orderTable);
        }
    }

    public void clearOrderTable() {
        orderTables.clearOrderTable();
    }

    public boolean isEmpty() {
        return orderTables.isEmpty();
    }

    protected void removeOrderTable(OrderTable orderTable) {
        this.orderTables.remove(orderTable);
    }

    protected void addOrderTable(OrderTable orderTable) {
        orderTables.add(orderTable);

        if (!orderTable.equalTableGroup(this)) {
            orderTable.setTableGroup(this);
        }
    }





    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TableGroup other = (TableGroup) o;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }






    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.get();
    }

    public void setOrderTables(final List<OrderTable> orderTables) {
//        this.orderTables = orderTables;
    }
}
