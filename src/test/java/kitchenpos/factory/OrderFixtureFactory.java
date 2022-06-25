package kitchenpos.factory;

import kitchenpos.domain.Order;

import java.time.LocalDateTime;

public class OrderFixtureFactory {
    public static Order createOrder(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime) {
        return new Order(id, orderTableId, orderStatus, orderedTime);
    }

    public static Order createOrder(Long orderTableId, String orderStatus, LocalDateTime orderedTime) {
        return new Order(orderTableId, orderStatus, orderedTime);
    }
}
