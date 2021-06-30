package kitchenpos.domain;

import java.util.List;
import java.util.Objects;

public class OrderTables {
    private List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public boolean isBookedAny() {
        for (OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
                return true;
            }
        }

        return false;
    }

    public int size() {
        return orderTables.size();
    }
}
