package kitchenpos.order.event;

import kitchenpos.orderstatus.domain.Status;
import kitchenpos.orderstatus.event.OrderStatusChangedEvent;

public class OrderStatusChangedEventImpl extends OrderStatusChangedEvent {

    private long orderId;
    private Status orderStatus;

    public OrderStatusChangedEventImpl(Object source, long orderId, Status orderStatus) {
        super(source);
        this.orderId = orderId;
        this.orderStatus = orderStatus;
    }

    @Override
    protected long getOrderId() {
        return 0;
    }

    @Override
    protected Status getChangedStatus() {
        return null;
    }
}
