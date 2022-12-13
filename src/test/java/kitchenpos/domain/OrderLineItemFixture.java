package kitchenpos.domain;

public class OrderLineItemFixture {
    private OrderLineItemFixture() {
    }

    public static OrderLineItem orderLineItemParam(long menuId, int quantity) {
        return new OrderLineItem(null, menuId, quantity);
    }

    public static OrderLineItem savedOrderLineItem(Long seq) {
        return new OrderLineItem(seq, 1L, 5);
    }
}
