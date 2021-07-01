package kitchenpos.fixture;

import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.Quantity;

public class OrderLineItemFixture {
    public static OrderLineItem 주문_연결_안된_양념치킨_콜라_1000원_1개;
    public static OrderLineItem 주문_연결_안된_양념치킨_콜라_1000원_2개;
    public static OrderLineItem 주문_연결_안된_후라이드치킨_콜라_2000원_1개;
    public static OrderLineItem 주문_연결_안된_후라이드치킨_콜라_2000원_2개;

    public static void cleanUp() {
        주문_연결_안된_양념치킨_콜라_1000원_1개 = new OrderLineItem(MenuFixture.양념치킨_콜라_1000원_1개, new Quantity(1L));
        주문_연결_안된_양념치킨_콜라_1000원_2개 = new OrderLineItem(MenuFixture.양념치킨_콜라_1000원_2개, new Quantity(1L));
        주문_연결_안된_후라이드치킨_콜라_2000원_1개 = new OrderLineItem(MenuFixture.후라이드치킨_콜라_2000원_1개, new Quantity(1L));
        주문_연결_안된_후라이드치킨_콜라_2000원_2개 = new OrderLineItem(MenuFixture.후라이드치킨_콜라_2000원_2개, new Quantity(1L));
    }
}
