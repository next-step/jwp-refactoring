package kitchenpos.tobe.orders.domain.domain;

import kitchenpos.tobe.common.domain.Validator;
import kitchenpos.tobe.orders.domain.order.Order;

public class FakeOrderValidator implements Validator<Order> {

    @Override
    public void validate(final Order order) {
    }
}
