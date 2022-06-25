package kitchenpos.domain;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemTest {

    public static OrderLineItem 주문_목록_생성(Long orderId, Long menuId, long quantity) {
        OrderLineItem result = new OrderLineItem();

        result.setOrderId(orderId);
        result.setMenuId(menuId);
        result.setQuantity(quantity);

        return result;
    }
}
