package kitchenpos.ordertable.event;

import kitchenpos.ordertable.domain.OrderTable;

public class OderTableUngroupedEvent {

    private Long orderTableId;
    public OderTableUngroupedEvent(OrderTable orderTable) {
        orderTableId = orderTable.getId();
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
