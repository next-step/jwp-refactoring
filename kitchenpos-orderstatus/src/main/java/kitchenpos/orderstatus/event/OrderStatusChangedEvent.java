package kitchenpos.orderstatus.event;

import kitchenpos.orderstatus.domain.Status;
import org.springframework.context.ApplicationEvent;

public abstract class OrderStatusChangedEvent extends ApplicationEvent {

    protected OrderStatusChangedEvent(Object source) {
        super(source);
    }

    protected abstract long getOrderId();

    protected abstract Status getChangedStatus();
}
