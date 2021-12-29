package domain.order;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

import java.util.Arrays;


public class OrderFixtures {
    public static Order 주문() {
        return new Order(1L, Arrays.asList(OrderLineItemFixtures.주문정보(), OrderLineItemFixtures.주문정보()));
    }

    public static OrderStatus 식사중() {
        return OrderStatus.MEAL;
    }

    public static OrderStatus 식사완료() {
        return OrderStatus.COMPLETION;
    }

    public static OrderStatus 준비중() {
        return OrderStatus.COOKING;
    }
}
