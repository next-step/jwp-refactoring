package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.error.ErrorEnum;
import kitchenpos.order.domain.Order;
import kitchenpos.ordertable.domain.OrderTable;
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

    protected TableGroup() {}

    public TableGroup(Long id, LocalDateTime createdDate, OrderTables orderTables) {
        validateTableGroup(orderTables);
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    private void validateTableGroup(OrderTables orderTables) {
        boolean hasNotEmpty = orderTables.get().stream()
                .anyMatch(orderTable -> !orderTable.isEmpty());
        if (hasNotEmpty) {
            throw new IllegalArgumentException(ErrorEnum.EXISTS_NOT_EMPTY_ORDER_TABLE.message());
        }

        boolean hasGroup = orderTables.get().stream()
                .anyMatch(orderTable -> orderTable.getTableGroup() != null);
        if (hasGroup) {
            throw new IllegalArgumentException(ErrorEnum.ALREADY_GROUP.message());
        }

    }

    public TableGroup(LocalDateTime createdDate, OrderTables orderTables) {
        validateTableGroup(orderTables);
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

    public List<Long> getOrderTableIds() {
        return orderTables.get()
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public void ungroup(List<Order> orders) {
        orders.forEach(Order::validateOrderStatusShouldComplete);
        orderTables.ungroup();
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
