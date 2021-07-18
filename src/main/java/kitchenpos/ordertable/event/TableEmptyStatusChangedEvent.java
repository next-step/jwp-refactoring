package kitchenpos.ordertable.event;

import kitchenpos.ordertable.dto.OrderTableRequest;

public class TableEmptyStatusChangedEvent {
    private final OrderTableRequest orderTableRequest;

    public TableEmptyStatusChangedEvent(OrderTableRequest orderTableRequest){
        this.orderTableRequest = orderTableRequest;
    }

    public OrderTableRequest getOrderTableRequest() {
        return orderTableRequest;
    }
}
