package common;

import static java.util.Arrays.asList;
import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.TAKE;

import java.time.LocalDateTime;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderFixture {

    public static Order 주문_첫번째() {
        return new Order(1L, 1L, TAKE.name(), LocalDateTime.now(),
            asList(new OrderLineItem(1L, 1L, 1L)));
    }

    public static Order 주문_첫번째_완료() {
        return new Order(1L, 1L, COMPLETION.name(), LocalDateTime.now(),
            asList(new OrderLineItem(1L, 1L, 1L)));
    }

    public static Order 주문_두번째() {
        return new Order(2L, 2L, TAKE.name(), LocalDateTime.now(),
            asList(new OrderLineItem(2L, 2L, 1L)));
    }
}
