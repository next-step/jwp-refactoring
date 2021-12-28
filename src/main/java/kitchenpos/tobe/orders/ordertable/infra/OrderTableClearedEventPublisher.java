package kitchenpos.tobe.orders.ordertable.infra;

import kitchenpos.tobe.common.domain.CustomEventPublisher;
import kitchenpos.tobe.orders.ordertable.domain.OrderTable;
import kitchenpos.tobe.orders.ordertable.domain.OrderTableClearedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class OrderTableClearedEventPublisher implements CustomEventPublisher<OrderTable> {

    private final ApplicationEventPublisher applicationEventPublisher;

    public OrderTableClearedEventPublisher(
        final ApplicationEventPublisher applicationEventPublisher
    ) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(final OrderTable orderTable) {
        applicationEventPublisher.publishEvent(
            new OrderTableClearedEvent(this, orderTable.getId())
        );
    }
}
