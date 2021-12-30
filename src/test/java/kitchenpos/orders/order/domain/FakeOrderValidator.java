package kitchenpos.orders.order.domain;

import kitchenpos.common.domain.Validator;
import kitchenpos.orders.order.domain.Order;

public class FakeOrderValidator implements Validator<Order> {

    @Override
    public void validate(final Order order) {
    }
}
