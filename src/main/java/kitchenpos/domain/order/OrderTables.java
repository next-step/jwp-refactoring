package kitchenpos.domain.order;

import kitchenpos.domain.product.TableGroup;
import kitchenpos.exception.BadRequestException;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static kitchenpos.utils.Message.*;

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
        checkOrderTableSize();
        checkAllOrderTablesAreEmpty();
        checkAllOrderTablesAreNotTableGroup();
        orderTables.forEach(it -> it.registerTableGroup(tableGroup));
    }

    private void checkAllUnGroupableOrderStatus() {
        if (anyMatchedBy(Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new BadRequestException(INVALID_CANCEL_ORDER_TABLES_STATUS);
        }
    }

    private boolean anyMatchedBy(List<OrderStatus> orderStatuses) {
        return orderTables.stream()
                .map(OrderTable::getOrders)
                .anyMatch(it -> it.anyMatchedIn(orderStatuses));
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
        checkAllUnGroupableOrderStatus();
        orderTables.forEach(OrderTable::unGroup);
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
