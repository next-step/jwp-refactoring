package kitchenpos.order;

import kitchenpos.order.domain.Order;

import java.util.Arrays;

import static kitchenpos.order.OrderLineItemFixtures.주문정보;


public class OrderFixtures {
    public static Order 주문() {
        return new Order(1L, Arrays.asList(주문정보(), 주문정보()));
    }
}
