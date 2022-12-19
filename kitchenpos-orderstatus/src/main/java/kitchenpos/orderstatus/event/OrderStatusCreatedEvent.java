package kitchenpos.orderstatus.event;

import kitchenpos.orderstatus.domain.Status;
import org.springframework.context.ApplicationEvent;

public abstract class OrderStatusCreatedEvent extends ApplicationEvent {

    protected OrderStatusCreatedEvent(Object source) {
        super(source);
    }

    protected abstract long getOrderId();

    protected abstract long getOrderTableId();

    protected abstract Status getOrderStatus();
}
