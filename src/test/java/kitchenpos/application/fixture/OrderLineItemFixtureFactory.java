package kitchenpos.application.fixture;


import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixtureFactory {

    private OrderLineItemFixtureFactory() {}

    public static OrderLineItem create(Long orderId, Long menuId, long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);

        return orderLineItem;
    }

    public static OrderLineItem create(Long seq, Long orderId, Long menuId, long quantity) {
        OrderLineItem orderLineItem = create(orderId, menuId, quantity);
        orderLineItem.setSeq(seq);
        return orderLineItem;
    }
}
