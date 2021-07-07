package kitchenpos.domain;

import static kitchenpos.domain.MenuTest.*;
import static kitchenpos.domain.OrderTest.*;

public class OrderLineItemTest {
    public static final OrderLineItem 테이블9주문_1 = new OrderLineItem(1L, 테이블9주문, 반반치킨_메뉴, 1);
    public static final OrderLineItem 테이블10주문_1 = new OrderLineItem(2L, 테이블10주문, 후라이드_메뉴, 1);
    public static final OrderLineItem 테이블10주문_2 = new OrderLineItem(3L, 테이블10주문, 양념치킨_메뉴, 1);
    public static final OrderLineItem 테이블11주문_1 = new OrderLineItem(4L, 테이블11주문, 순살치킨_메뉴, 1);
}
