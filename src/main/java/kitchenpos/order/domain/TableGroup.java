package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.constant.ErrorCode;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;
    @Embedded
    private OrderTables orderTables;

    public TableGroup() {}

    private TableGroup(Long id, OrderTables orderTables) {
        validateOrderTableHasNotGroupId(orderTables);
        orderTables.updateTableGroup(this);
        this.id = id;
        this.createdDate = LocalDateTime.now();
        this.orderTables = orderTables;
    }

    public static TableGroup of(Long id, List<OrderTable> orderTables) {
        return new TableGroup(id, OrderTables.from(orderTables));
    }

    public static TableGroup from(List<OrderTable> orderTables) {
        return new TableGroup(null, OrderTables.from(orderTables));
    }

    private void validateOrderTableHasNotGroupId(OrderTables orderTables) {
        if(orderTables.anyHasGroupId()) {
            throw new IllegalArgumentException(ErrorCode.주문_테이블에_이미_단체_그룹_지정됨.getErrorMessage());
        }
    }

    public void ungroup(List<Order> orders) {
        orders.forEach(Order::validateIfNotCompletionOrder);
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
