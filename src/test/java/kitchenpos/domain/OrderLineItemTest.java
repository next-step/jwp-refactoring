package kitchenpos.domain;

import static kitchenpos.domain.MenuTest.*;
import static kitchenpos.domain.OrderStatus.*;
import static kitchenpos.domain.OrderTableTest.*;
import static kitchenpos.domain.OrderTest.*;

public class OrderLineItemTest {
    public static final OrderLineItem 테이블9주문_1 = new OrderLineItem(1L, 1L, 반반치킨_메뉴.getId(), 1);
    public static final OrderLineItem 테이블10주문_1 = new OrderLineItem(2L, 2L, 후라이드_메뉴.getId(), 1);
    public static final OrderLineItem 테이블10주문_2 = new OrderLineItem(3L, 2L, 양념치킨_메뉴.getId(), 1);
    public static final OrderLineItem 테이블11주문_1 = new OrderLineItem(4L, 3L, 순살치킨_메뉴.getId(), 1);
}
