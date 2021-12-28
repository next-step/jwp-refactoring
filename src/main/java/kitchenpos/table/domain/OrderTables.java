package kitchenpos.table.domain;

import kitchenpos.common.exception.InvalidOrderTableException;
import kitchenpos.common.exception.InvalidTableGroupSizeException;
import kitchenpos.common.exception.NotFoundOrderTableException;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Embeddable
public class OrderTables {
    private static final int MIN_TABLE_SIZE = 2;
    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.MERGE)
    private final List<OrderTable> orderTables;

    protected OrderTables() {
        this.orderTables = new ArrayList<>();
    }

    private OrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static OrderTables ofCreate(final List<OrderTable> orderTables) {
        validateOrderTable(orderTables);
        return new OrderTables(orderTables);
    }

    public static OrderTables ofUngroup(final List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    public static void validateOrderTable(final List<OrderTable> orderTables) {
        if (isInvalidTableSize(orderTables)) {
            throw new InvalidTableGroupSizeException();
        }
        if (validateOrderTables(orderTables)) {
            throw new InvalidOrderTableException();
        }
    }

    private static boolean isInvalidTableSize(final List<OrderTable> orderTables) {
        return CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_TABLE_SIZE;
    }

    private static boolean validateOrderTables(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .anyMatch(OrderTable::isNotEmptyOrNonNullTableGroup);
    }

    public void initTableGroup(final Long tableGroupId, List<Long> orderTableIds) {
        if (isNotFoundOrderTables(orderTableIds)) {
            throw new NotFoundOrderTableException();
        }
        orderTables.forEach(orderTable -> orderTable.initTableGroup(tableGroupId));
    }

    private boolean isNotFoundOrderTables(List<Long> orderTableIds) {
        return orderTableIds.size() != orderTables.size();
    }

    public int size() {
        return orderTables.size();
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::unTableGroup);
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public List<Long> getTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTables that = (OrderTables) o;
        return Objects.equals(orderTables, that.orderTables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTables);
    }
}
