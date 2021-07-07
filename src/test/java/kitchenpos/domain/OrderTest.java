package kitchenpos.domain;

import static java.util.Arrays.*;
import static kitchenpos.domain.OrderLineItemTest.*;
import static kitchenpos.domain.OrderStatus.*;
import static kitchenpos.domain.OrderTableTest.*;

public class OrderTest {
    public static final Order2 테이블9주문 = new Order2(1L, 테이블9_사용중, COOKING, asList(테이블9주문_1));
    public static final Order2 테이블10주문 = new Order2(2L, 테이블10_사용중, MEAL, asList(테이블10주문_1, 테이블10주문_2));
    public static final Order2 테이블11주문 = new Order2(3L, 테이블11_사용중, COMPLETION, asList(테이블11주문_1));
}
