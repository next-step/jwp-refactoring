package kitchenpos.domain;

import static kitchenpos.domain.OrderLineItemTest.*;
import static kitchenpos.domain.OrderStatus.*;
import static kitchenpos.domain.OrderTableTest.*;

public class OrderTest {
    public static final Order 테이블9주문 = new Order(1L, COOKING, OrderLineItems.of(테이블9주문_1));
    public static final Order 테이블10주문 = new Order(2L, MEAL, OrderLineItems.of(테이블10주문_1, 테이블10주문_2));
    public static final Order 테이블11주문 = new Order(3L, COMPLETION, OrderLineItems.of(테이블11주문_1));

    static {
        테이블9_사용중.addOrder(테이블9주문);
        테이블10_사용중.addOrder(테이블10주문);
        테이블11_사용중.addOrder(테이블11주문);
    }
}
