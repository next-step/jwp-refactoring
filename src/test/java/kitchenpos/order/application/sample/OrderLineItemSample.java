package kitchenpos.order.application.sample;

import static kitchenpos.menu.sample.MenuSample.이십원_후라이드치킨_두마리세트;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.spy;

import kitchenpos.common.domain.Quantity;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemSample {

    public static OrderLineItem 이십원_후라이트치킨_두마리세트_한개_주문_항목() {
        OrderLineItem lineItem = spy(OrderLineItem.of(Quantity.from(1L), 이십원_후라이드치킨_두마리세트()));
        lenient().when(lineItem.seq())
            .thenReturn(1L);
        return lineItem;
    }
}
