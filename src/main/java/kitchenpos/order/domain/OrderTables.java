package kitchenpos.order.domain;

import kitchenpos.order.exception.TableGroupExceptionCode;
import org.springframework.util.CollectionUtils;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class OrderTables {
    private static final int MIN_ORDER_TABLES_SIZE = 2;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {}

    public void group(TableGroup tableGroup, List<OrderTable> target) {
        validate(target);
        target.forEach(orderTable -> {
            orderTable.checkOrderTableForTableGrouping();
            orderTable.changeEmpty(false, Collections.emptyList());
            addOrderTable(tableGroup, orderTable);
        });
    }

    private void validate(List<OrderTable> target) {
        if (CollectionUtils.isEmpty(target)) {
            throw new IllegalArgumentException(TableGroupExceptionCode.ORDER_TABLES_CANNOT_BE_EMPTY.getMessage());
        }

        if (target.size() < MIN_ORDER_TABLES_SIZE) {
            throw new IllegalArgumentException(TableGroupExceptionCode.MUST_BE_GREATER_THAN_MINIMUM_SIZE.getMessage());
        }
    }

    public void addOrderTable(TableGroup tableGroup, OrderTable orderTable) {
        if (!hasOrderTable(orderTable)) {
            this.orderTables.add(orderTable);
            orderTable.updateTableGroup(tableGroup);
        }
    }

    private boolean hasOrderTable(OrderTable orderTable) {
        return this.orderTables.contains(orderTable);
    }

    public void ungroup() {
        orderTables.forEach(orderTable -> orderTable.ungroup());
    }

    public List<OrderTable> getOrderTables() {
        return Collections.unmodifiableList(orderTables);
    }
}
