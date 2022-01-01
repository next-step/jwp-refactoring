package kitchenpos.ordertable.dto;

import java.util.List;
import kitchenpos.ordertable.domain.TableGroup;

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
