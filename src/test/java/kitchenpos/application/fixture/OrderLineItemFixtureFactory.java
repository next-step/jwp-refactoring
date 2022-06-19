package kitchenpos.application.fixture;


import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemFixtureFactory {

    private OrderLineItemFixtureFactory() {}

    public static OrderLineItem create(Long menuId, long quantity) {
        return OrderLineItem.of(menuId, quantity);
    }
}
