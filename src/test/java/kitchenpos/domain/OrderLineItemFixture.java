package kitchenpos.domain;

public class OrderLineItemFixture {
    public static OrderLineItem createOrderLineItem(Long seq, Long orderId, Long menuId, long quantity) {
        return new OrderLineItem(seq, orderId, menuId, quantity);
    }

    public static OrderLineItem createOrderLineItem(Long menuId, long quantity) {
        return new OrderLineItem(null, null, menuId, quantity);
    }
}
