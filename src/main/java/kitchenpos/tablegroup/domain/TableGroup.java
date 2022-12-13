package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.constant.ErrorCode;
import kitchenpos.order.domain.Order;
import kitchenpos.ordertable.domain.OrderTables;
import org.springframework.data.annotation.CreatedDate;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;

    public TableGroup() {}

    public TableGroup(OrderTables orderTables) {
        validateOrderTableHasNotGroupId(orderTables);
        orderTables.updateTableGroup(this);
        this.createdDate = LocalDateTime.now();
        this.orderTables = orderTables;
    }

    public TableGroup(Long id, OrderTables orderTables) {
        validateOrderTableHasNotGroupId(orderTables);
        orderTables.updateTableGroup(this);
        this.id = id;
        this.createdDate = LocalDateTime.now();
        this.orderTables = orderTables;
    }

    private void validateOrderTableHasNotGroupId(OrderTables orderTables) {
        if(orderTables.anyHasGroupId()) {
            throw new IllegalArgumentException(ErrorCode.HAS_TABLE_GROUP.getErrorMessage());
        }
    }

    public void ungroup(List<Order> orders) {
        orders.forEach(Order::validateNotCompleteOrder);
        orderTables.ungroupOrderTables();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
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
