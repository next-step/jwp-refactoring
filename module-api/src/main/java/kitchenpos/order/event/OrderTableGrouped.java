package kitchenpos.order.event;

import kitchenpos.order.domain.OrderTable;

import java.util.List;

public class OrderTableGrouped {
    private Long tableGroupId;
    private List<Long> orderTableIds;

    public OrderTableGrouped(Long tableGroupId, List<Long> orderTableIds) {
        this.tableGroupId = tableGroupId;
        this.orderTableIds = orderTableIds;
    }

    public static OrderTableGrouped from(Long tableGroupId, List<Long> orderTableIds) {
        return new OrderTableGrouped(tableGroupId, orderTableIds);
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
