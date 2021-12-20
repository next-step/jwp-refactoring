package kitchenpos.application.fixture;

import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemFixture {

    private OrderLineItemFixture() {
    }

    public static OrderLineItem 주문항목_생성(Long orderId, Long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(orderId);
        orderLineItem.setQuantity(quantity);

        return orderLineItem;
    }
}
