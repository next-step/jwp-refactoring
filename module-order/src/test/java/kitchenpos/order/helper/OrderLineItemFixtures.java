package kitchenpos.order.helper;

import kitchenpos.order.dto.OrderLineItemRequest;

public class OrderLineItemFixtures {

    public static final OrderLineItemRequest 주문_항목_요청1 = 주문_항목_요청_만들기(1L, 1);
    public static final OrderLineItemRequest 주문_항목_요청2 = 주문_항목_요청_만들기(2L, 2);
    public static final OrderLineItemRequest 없는_주문_항목_요청 = 주문_항목_요청_만들기(9999999L, 2);

    public static OrderLineItemRequest 주문_항목_요청_만들기(Long menuId, Integer quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }
}
