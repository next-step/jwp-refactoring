package kitchenpos.ordertable.domain;

import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.exception.BadRequestException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static kitchenpos.utils.Message.*;

public class OrderTables {

    private static final int MIN_ORDER_TABLE_SIZE = 2;

    private List<OrderTable> orderTables;

    private OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static OrderTables from(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    public List<OrderTable> getOrderTables() {
        return Collections.unmodifiableList(orderTables);
    }

    public void registerTableGroup(TableGroup tableGroup) {
        checkOrderTableSize();
        checkAllOrderTablesAreEmpty();
        checkAllOrderTablesAreNotTableGroup();
        orderTables.forEach(it -> it.registerTableGroup(tableGroup.getId()));
    }

    private void checkOrderTableSize() {
        if (orderTables.isEmpty() || orderTables.size() < MIN_ORDER_TABLE_SIZE) {
            throw new BadRequestException(INVALID_ORDER_TABLE_SIZE);
        }
    }

    private void checkAllOrderTablesAreEmpty() {
        boolean isNotEmpty = orderTables.stream().anyMatch(it -> !it.isEmpty());

        if (isNotEmpty) {
            throw new BadRequestException(INVALID_EMPTY_ORDER_TABLE);
        }
    }

    private void checkAllOrderTablesAreNotTableGroup() {
        boolean alreadyRegistered = orderTables.stream().anyMatch(it -> Objects.nonNull(it.getTableGroupId()));

        if (alreadyRegistered) {
            throw new BadRequestException(CONTAIN_ALREADY_GROUPED_ORDER_TABLE);
        }
    }

    public void unGroup() {
        orderTables.forEach(OrderTable::unGroup);
    }

    public List<Long> getOrderTableIds() {
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
