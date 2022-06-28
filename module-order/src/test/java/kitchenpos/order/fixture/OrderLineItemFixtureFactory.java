package kitchenpos.order.fixture;


import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemFixtureFactory {

    private OrderLineItemFixtureFactory() {}

    public static OrderLineItem create(Long menuId, long quantity) {
        return OrderLineItem.of(menuId, quantity);
    }
}
