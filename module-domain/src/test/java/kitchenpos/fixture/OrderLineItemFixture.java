package kitchenpos.fixture;

import kitchenpos.domain.Quantity;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemFixture {
    public static OrderLineItem 주문_연결_안된_양념치킨_콜라_1000원_1개;
    public static OrderLineItem 주문_연결_안된_양념치킨_콜라_1000원_2개;
    public static OrderLineItem 주문_연결_안된_후라이드치킨_콜라_2000원_1개;
    public static OrderLineItem 주문_연결_안된_후라이드치킨_콜라_2000원_2개;

    public static void cleanUp() {
        주문_연결_안된_양념치킨_콜라_1000원_1개 = createOrderLineItem(MenuFixture.양념치킨_콜라_1000원_1개, new Quantity(1L));
        주문_연결_안된_양념치킨_콜라_1000원_2개 = createOrderLineItem(MenuFixture.양념치킨_콜라_1000원_2개, new Quantity(1L));
        주문_연결_안된_후라이드치킨_콜라_2000원_1개 = createOrderLineItem(MenuFixture.후라이드치킨_콜라_2000원_1개, new Quantity(1L));
        주문_연결_안된_후라이드치킨_콜라_2000원_2개 = createOrderLineItem(MenuFixture.후라이드치킨_콜라_2000원_2개, new Quantity(1L));
    }

    private static OrderLineItem createOrderLineItem(Menu menu, Quantity quantity) {
        return new OrderLineItem(null, menu.getId(), menu.getName(), menu.getPrice(), quantity);
    }
}
