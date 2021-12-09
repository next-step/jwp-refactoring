package kitchenpos.application.fixture;

import static kitchenpos.application.fixture.MenuFixture.*;
import static kitchenpos.application.fixture.OrderTableFixture.*;

import java.time.LocalDateTime;
import java.util.Arrays;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public class OrderFixture {
    public static Order 불고기_주문 = new Order();
    public static Order 불고기_식사중_주문 = new Order();
    public static OrderLineItem 불고기_주문항목 = new OrderLineItem();

    static {
        // Order
        불고기_주문.setId(1L);
        불고기_주문.setOrderTableId(주문_개인테이블.getId());
        불고기_주문.setOrderStatus(OrderStatus.COOKING.name());
        불고기_주문.setOrderedTime(LocalDateTime.now());
        불고기_주문.setOrderLineItems(Arrays.asList(불고기_주문항목));

        불고기_식사중_주문.setId(2L);
        불고기_식사중_주문.setOrderTableId(주문_개인테이블.getId());
        불고기_식사중_주문.setOrderStatus(OrderStatus.MEAL.name());
        불고기_식사중_주문.setOrderedTime(LocalDateTime.now());
        불고기_식사중_주문.setOrderLineItems(Arrays.asList(불고기_주문항목));

        // OrderLineItem
        불고기_주문항목.setSeq(1L);
        불고기_주문항목.setOrderId(불고기_주문.getId());
        불고기_주문항목.setMenuId(불고기.getId());
        불고기_주문항목.setQuantity(1L);
    }

    private OrderFixture() {}
}
