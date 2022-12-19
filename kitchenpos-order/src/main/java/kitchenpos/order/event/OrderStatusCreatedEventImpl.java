package kitchenpos.order.event;

import kitchenpos.orderstatus.domain.Status;
import kitchenpos.orderstatus.event.OrderStatusCreatedEvent;

public class OrderStatusCreatedEventImpl extends OrderStatusCreatedEvent {

    private long orderId;
    private long orderTableId;
    private Status orderStatus;

    public OrderStatusCreatedEventImpl(Object source, long orderId, long orderTableId, Status orderStatus) {
        super(source);
        this.orderId = orderId;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
    }

    @Override
    protected long getOrderId() {
        return 0;
    }

    @Override
    protected long getOrderTableId() {
        return 0;
    }

    @Override
    protected Status getOrderStatus() {
        return null;
    }
}
