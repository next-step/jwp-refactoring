package kitchenpos.domain;

import static kitchenpos.domain.MenuTest.*;

public class OrderLineItemTest {
    public static final OrderLineItem 테이블9주문_1 = new OrderLineItem(1L, 반반치킨_메뉴, 반반치킨_메뉴.getName(), 반반치킨_메뉴.getPrice(), Quantity.valueOf(1));
    public static final OrderLineItem 테이블10주문_1 = new OrderLineItem(2L, 후라이드_메뉴, 후라이드_메뉴.getName(), 후라이드_메뉴.getPrice(), Quantity.valueOf(1));
    public static final OrderLineItem 테이블10주문_2 = new OrderLineItem(3L, 양념치킨_메뉴, 양념치킨_메뉴.getName(), 양념치킨_메뉴.getPrice(), Quantity.valueOf(1));
    public static final OrderLineItem 테이블11주문_1 = new OrderLineItem(4L, 순살치킨_메뉴, 순살치킨_메뉴.getName(), 순살치킨_메뉴.getPrice(), Quantity.valueOf(1));
}
