package kitchenpos.order.domain;

import static kitchenpos.menu.acceptance.MenuAcceptanceTestUtils.메뉴_면류_짜장면;
import static kitchenpos.menu.domain.MenuTestFixture.*;

import java.util.Collections;
import java.util.List;
import kitchenpos.order.dto.OrderLineItemRequest;

public class OrderLineItemTestFixture {
    public static final OrderLineItem 짜장_탕수육_주문_항목 = orderLineItem(1L,  짜장_탕수육_주문_세트, 1L);
    public static final OrderLineItem 짬뽕2_탕수육_주문_항목 = orderLineItem(2L, 짬뽕2_탕수육_주문_세트, 2L);

    public static List<OrderLineItemRequest> 짜장면_1그릇_주문_항목() {
        return Collections.singletonList(orderLineItemRequest(메뉴_면류_짜장면().getId(), 1L));
    }

    public static OrderLineItemRequest orderLineItemRequest(Long menuId, Long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }

    public static OrderLineItem orderLineItem(Long seq, OrderMenu menu, long quantity) {
        return OrderLineItem.of(seq, null, menu, quantity);
    }
}
