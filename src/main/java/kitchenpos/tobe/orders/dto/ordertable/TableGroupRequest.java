package kitchenpos.tobe.orders.dto.ordertable;

import java.util.List;
import kitchenpos.tobe.orders.domain.ordertable.TableGroup;

public class TableGroupRequest {

    private final List<Long> orderTableIds;

    public TableGroupRequest(final List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    public TableGroup toTableGroup() {
        return new TableGroup();
    }
}
