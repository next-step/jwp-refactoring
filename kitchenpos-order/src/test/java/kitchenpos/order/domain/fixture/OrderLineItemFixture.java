package kitchenpos.order.domain.fixture;

import kitchenpos.order.domain.*;

import java.math.BigDecimal;

public class OrderLineItemFixture {

    public static OrderLineItem OrderLineItem() {
        return new OrderLineItem(null, OrderMenu.of(1L, new Name("A"), new Price(BigDecimal.ONE)), new Quantity(1));
    }
}
