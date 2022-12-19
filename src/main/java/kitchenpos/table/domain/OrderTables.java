package kitchenpos.table.domain;

import kitchenpos.ExceptionMessage;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class OrderTables {

    public static final int MINIMUM_SIZE = 2;

    private List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MINIMUM_SIZE) {
            throw new IllegalArgumentException(ExceptionMessage.ORDER_TABLE_MINIMUM_SIZE
                    .getMessage("" + MINIMUM_SIZE));
        }
        orderTables.stream()
                .forEach(OrderTable::validateTableGroup);
        this.orderTables = orderTables;
    }

    public void group(TableGroup tableGroup) {
        orderTables.forEach(orderTable -> orderTable.group(tableGroup));
    }

    public List<OrderTable> getValue() {
        return orderTables;
    }
}
