package kitchenpos.domain;

public class OrderLineItemFixture {
    private OrderLineItemFixture() {
    }

    public static OrderLineItem savedOrderLineItem(Long seq, Long orderId) {
        return new OrderLineItem(seq, orderId, 1L, 5);
    }

    public static OrderLineItem savedOrderLineItemParam(Long seq, Long orderId, long menuId, int quantity) {
        return new OrderLineItem(seq, orderId, menuId, quantity);
    }

    public static OrderLineItem orderLineItemParam(long menuId, int quantity) {
        return new OrderLineItem(null, null, menuId, quantity);
    }

}
