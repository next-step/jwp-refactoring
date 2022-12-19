package kitchenpos.order.domain;

import kitchenpos.exception.ErrorMessage;
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

    public void ungroup() {
        orderTables.forEach(OrderTable::ungroup);
    }

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
            throw new IllegalArgumentException(ErrorMessage.TABLE_GROUP_ORDER_TABLES_CANNOT_BE_EMPTY.getMessage());
        }
        if (target.size() < MIN_ORDER_TABLES_SIZE) {
            throw new IllegalArgumentException(ErrorMessage.TABLE_GROUP_MUST_BE_GREATER_THAN_MINIMUM_SIZE.getMessage());
        }
    }

    public void addOrderTable(TableGroup tableGroup, OrderTable orderTable) {
        if (!isContains(orderTable)) {
            this.orderTables.add(orderTable);
            orderTable.updateTableGroup(tableGroup);
        }
    }

    private boolean isContains(OrderTable orderTable) {
        return this.orderTables.contains(orderTable);
    }

    public List<OrderTable> values() {
        return Collections.unmodifiableList(orderTables);
    }
}
