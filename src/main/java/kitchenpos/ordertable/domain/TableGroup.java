package kitchenpos.ordertable.domain;

import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.entity.BaseTimeEntity;

@Entity
public class TableGroup extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private OrderTables orderTables = new OrderTables();

    public TableGroup() {
    }

    public TableGroup(Long id) {
        this.id = id;
    }

    public void groupTables(List<OrderTable> inputOrderTables) {
        orderTables.groupTables(inputOrderTables);
        for (OrderTable orderTable : inputOrderTables) {
            orderTable.groupIn(this);
            orderTable.changeOrderClose(false);
        }
    }

    public void ungroup() {
        orderTables.ungroup();
    }

    public Long getId() {
        return id;
    }

    public OrderTables getOrderTables() {
        return orderTables;
    }

    public List<OrderTable> getOrderTableList() {
        return orderTables.getOrderTables();
    }

    public int getOrderTableSize() {
        return orderTables.getSize();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof TableGroup)) {
            return false;
        }

        TableGroup that = (TableGroup) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
