package kitchenpos.table.domain;

import kitchenpos.ExceptionMessage;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

public class OrderTables {

    public static final int MINIMUM_SIZE = 2;

    private List<OrderTable> orderTables;

    protected OrderTables() {
        orderTables = Arrays.asList();
    }

    public OrderTables(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MINIMUM_SIZE) {
            throw new IllegalArgumentException(ExceptionMessage.ORDER_TABLE_MINIMUM_SIZE
                    .getMessage("" + MINIMUM_SIZE));
        }
        orderTables.stream()
                .forEach(OrderTable::validateTableGroup);
        this.orderTables = orderTables;
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::ungroup);
    }

    public void group(TableGroup tableGroup) {
        orderTables.forEach(orderTable -> orderTable.group(tableGroup));
    }

    public List<OrderTable> getValue() {
        return orderTables;
    }
}
