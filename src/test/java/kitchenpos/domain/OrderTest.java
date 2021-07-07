package kitchenpos.domain;

import static java.util.Arrays.*;
import static kitchenpos.domain.OrderLineItemTest.*;
import static kitchenpos.domain.OrderTableTest.*;

public class OrderTest {
    public static final Order 테이블9주문 = new Order(1L, 테이블9_사용중.getId(), "COOKING", asList(테이블9주문_1));
    public static final Order 테이블10주문 = new Order(2L, 테이블10_사용중.getId(), "MEAL", asList(테이블10주문_1, 테이블10주문_2));
    public static final Order 테이블11주문 = new Order(3L, 테이블11_사용중.getId(), "COMPLETION", asList(테이블11주문_1));
}
