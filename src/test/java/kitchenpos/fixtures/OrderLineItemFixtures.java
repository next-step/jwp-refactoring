package kitchenpos.fixtures;

import kitchenpos.domain.OrderLineItem;

/**
 * packageName : kitchenpos.fixtures
 * fileName : OrderLineItemFixtures
 * author : haedoang
 * date : 2021/12/18
 * description :
 */
public class OrderLineItemFixtures {
    public static OrderLineItem createOrderLineItem(Long seq, Long orderId, Long menuId, long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(seq);
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }
}
