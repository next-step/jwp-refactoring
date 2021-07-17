package kitchenpos.order.domain;

import static kitchenpos.order.domain.OrderLineItemTest.*;

public class OrderTest {
    public static final Order 테이블9주문_조리 = new Order(1L, OrderLineItems.of(테이블9주문_1));
    public static final Order 테이블10주문_식사 = new Order(2L, OrderLineItems.of(테이블10주문_1, 테이블10주문_2));
    public static final Order 테이블11주문_완결 = new Order(3L, OrderLineItems.of(테이블11주문_1));

    public static Order order(Long id, OrderLineItems orderLineItems) {
        return new Order(id, orderLineItems);
    }
}
