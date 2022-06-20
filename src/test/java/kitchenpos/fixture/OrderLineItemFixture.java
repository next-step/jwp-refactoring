package kitchenpos.fixture;

import kitchenpos.domain.OrderLineItem;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderLineItemFixture {

    public static OrderLineItem 주문항목_데이터_생성() {
        return new OrderLineItem(1L, 1L, 1L, 1);
    }

    public static void 주문항목_데이터_확인(OrderLineItem orderLineItem, Long seq, Long orderId, Long menuId, int quantity) {
        assertAll(
                () -> assertEquals(seq, orderLineItem.getSeq()),
                () -> assertEquals(orderId, orderLineItem.getOrderId()),
                () -> assertEquals(menuId, orderLineItem.getMenuId()),
                () -> assertEquals(quantity, orderLineItem.getQuantity())
        );
    }

}
