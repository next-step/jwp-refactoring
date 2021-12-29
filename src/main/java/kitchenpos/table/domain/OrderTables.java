package kitchenpos.table.domain;

import static javax.persistence.CascadeType.ALL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import kitchenpos.common.exception.Message;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTables {

    private static final int MIN_TABLE_SIZE = 2;

    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.LAZY, cascade = ALL)
    private List<OrderTable> orderTables = new ArrayList<>();

    public static OrderTables of(List<OrderTable> savedOrderTables) {
        return new OrderTables(savedOrderTables);
    }

    private OrderTables(List<OrderTable> orderTables) {
        if (isSmallThanMinTableSize(orderTables)) {
            throw new IllegalArgumentException(
                Message.ORDER_TABLES_IS_SMALL_THAN_MIN_TABLE_SIZE.getMessage());
        }

        if (matchOrderTable(this::isTableEmpty)) {
            throw new IllegalArgumentException(
                Message.ORDER_TABLE_IS_NOT_EMPTY_TABLE_OR_ALREADY_GROUP.getMessage()
            );
        }

        this.orderTables.addAll(orderTables);
    }

    public OrderTables() {
    }

    private boolean isSmallThanMinTableSize(final List<OrderTable> orderTables) {
        return CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_TABLE_SIZE;
    }

    public void group() {
        if (matchOrderTable(isTableIsNotEmptyOrGroupIsNotNull())) {
            throw new IllegalArgumentException(
                Message.ORDER_TABLE_IS_NOT_EMPTY_TABLE_OR_ALREADY_GROUP.getMessage());
        }

        TableGroup of = TableGroup.of();
        orderTables.stream()
            .forEach(s->s.group(of));
    }

    public void unGroup() {
        orderTables.stream()
            .forEach(OrderTable::unGroup);
    }

    private Predicate<OrderTable> isTableIsNotEmptyOrGroupIsNotNull() {
        return orderTable -> isTableEmpty(orderTable) || isGroupIsNotNull(orderTable);
    }

    private boolean isTableEmpty(OrderTable orderTable) {
        return OrderTableStatus.isEmpty(orderTable);
    }

    private boolean isGroupIsNotNull(OrderTable orderTable) {
        return Objects.nonNull(orderTable.getTableGroupId());
    }

    private boolean matchOrderTable(Predicate<OrderTable> orderTablePredicate) {
        return orderTables.stream()
            .anyMatch(orderTablePredicate);
    }

    public List<OrderTable> getList() {
        return Collections.unmodifiableList(orderTables);
    }

    public int size() {
        return orderTables.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderTables that = (OrderTables) o;
        return Objects.equals(orderTables, that.orderTables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTables);
    }


}

