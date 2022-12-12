package kitchenpos.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.exception.CannotGroupOrderTablesException;
import kitchenpos.exception.CannotUnGroupOrderTablesException;
import kitchenpos.exception.ExceptionMessage;

@Embeddable
public class OrderTables {

    private static final int MIN_ORDER_TABLE_SIZE = 2;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    protected OrderTables() {
    }

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
        checkOrderTableSizeGreaterThanMinSize();
        checkAllOrderTablesAreEmpty();
        checkAllOrderTablesNotYetRegisteredTableGroup();
        orderTables.forEach(it -> it.registerTableGroup(tableGroup));
    }

    public void unGroup() {
        checkAllUnGroupableOrderStatus();
        orderTables.forEach(OrderTable::unGroup);
    }

    private void checkAllUnGroupableOrderStatus() {
        if (anyMatchedBy(Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new CannotUnGroupOrderTablesException(ExceptionMessage.CAN_NOT_UN_GROUP_ORDER_TABLES);
        }
    }

    private boolean anyMatchedBy(List<OrderStatus> orderStatuses) {
        return orderTables.stream()
                .map(OrderTable::getOrders)
                .anyMatch(it -> it.anyMatchedIn(orderStatuses));
    }

    private void checkOrderTableSizeGreaterThanMinSize() {
        if (orderTables.isEmpty() || orderTables.size() < MIN_ORDER_TABLE_SIZE) {
            throw new CannotGroupOrderTablesException(ExceptionMessage.INVALID_ORDER_TABLE_SIZE);
        }
    }

    private void checkAllOrderTablesAreEmpty() {
        boolean isNotEmpty = orderTables.stream().anyMatch(it -> !it.isEmpty());

        if (isNotEmpty) {
            throw new CannotGroupOrderTablesException(ExceptionMessage.NOT_EMPTY_ORDER_TABLE_EXIST);
        }
    }

    private void checkAllOrderTablesNotYetRegisteredTableGroup() {
        boolean alreadyRegistered = orderTables.stream().anyMatch(it -> Objects.nonNull(it.getTableGroupId()));

        if (alreadyRegistered) {
            throw new CannotGroupOrderTablesException(ExceptionMessage.ALREADY_GROUPED_ORDER_TABLE_EXIST);
        }
    }
}
