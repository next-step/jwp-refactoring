package kitchenpos.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

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
            throw new IllegalArgumentException();
        }
    }

    private boolean anyMatchedBy(List<OrderStatus> orderStatuses) {
        return orderTables.stream()
                .map(OrderTable::getOrders)
                .anyMatch(it -> it.anyMatchedIn(orderStatuses));
    }

    private void checkOrderTableSizeGreaterThanMinSize() {
        if (orderTables.isEmpty() || orderTables.size() < MIN_ORDER_TABLE_SIZE) {
            throw new IllegalArgumentException();
        }
    }

    private void checkAllOrderTablesAreEmpty() {
        boolean isNotEmpty = orderTables.stream().anyMatch(it -> !it.isEmpty());

        if (isNotEmpty) {
            throw new IllegalArgumentException();
        }
    }

    private void checkAllOrderTablesNotYetRegisteredTableGroup() {
        boolean alreadyRegistered = orderTables.stream().anyMatch(it -> Objects.nonNull(it.getTableGroupId()));

        if (alreadyRegistered) {
            throw new IllegalArgumentException();
        }
    }
}
