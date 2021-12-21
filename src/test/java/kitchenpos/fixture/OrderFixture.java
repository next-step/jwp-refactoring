package kitchenpos.fixture;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

import java.time.LocalDateTime;

public class OrderFixture {
    public static final Order order = Order.of(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), null);
}
