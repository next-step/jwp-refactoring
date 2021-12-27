package kitchenpos.table.domain;

import kitchenpos.common.exception.InvalidOrderTableException;
import kitchenpos.common.exception.InvalidTableGroupSizeException;
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

    public OrderTables(final List<OrderTable> orderTables) {
        validateOrderTable(orderTables);
        this.orderTables = orderTables;
    }

    public void validateOrderTable(final List<OrderTable> orderTables) {
        if (isInvalidTableSize(orderTables)) {
            throw new InvalidTableGroupSizeException();
        }
        if (validateOrderTables(orderTables)) {
            throw new InvalidOrderTableException();
        }
    }

    private boolean isInvalidTableSize(final List<OrderTable> orderTables) {
        return CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_TABLE_SIZE;
    }

    private boolean validateOrderTables(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .anyMatch(this::isNotEmptyOrNonNullTableGroup);
    }

    private boolean isNotEmptyOrNonNullTableGroup(final OrderTable orderTable) {
        return !orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup());
    }

    public void initTableGroup(final TableGroup tableGroup) {
        orderTables.forEach(orderTable -> orderTable.initTableGroup(tableGroup));
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
