package kitchenpos.tablegroup.domain;

import kitchenpos.common.BaseEntity;
import kitchenpos.order.domain.Order;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Entity
public class TableGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private OrderTables orderTables = new OrderTables();

    public TableGroup() {}

    public void group(List<OrderTable> target) {
        this.orderTables.group(this, target);
    }

    void addOrderTable(OrderTable orderTable) {
        this.orderTables.addOrderTable(this, orderTable);
    }

    public void ungroup(List<Order> orders) {
        orders.forEach(Order::checkOrderStatus);
        this.orderTables.ungroup();
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.getOrderTables();
    }

    public List<Long> getOrderTableIds() {
        return orderTables.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(toList());
    }

    public boolean isDifferent(TableGroup tableGroup) {
        return this != tableGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TableGroup that = (TableGroup) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
