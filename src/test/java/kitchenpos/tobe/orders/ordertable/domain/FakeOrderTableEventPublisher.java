package kitchenpos.tobe.orders.ordertable.domain;

import kitchenpos.tobe.common.domain.CustomEventPublisher;

public class FakeOrderTableEventPublisher implements CustomEventPublisher<OrderTable> {

    @Override
    public void publish(final OrderTable orderTable) {
    }
}
