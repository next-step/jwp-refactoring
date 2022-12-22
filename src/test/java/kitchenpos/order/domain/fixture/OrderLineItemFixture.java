package kitchenpos.order.domain.fixture;

import kitchenpos.common.Name;
import kitchenpos.common.Price;
import kitchenpos.common.Quantity;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;

import java.math.BigDecimal;

public class OrderLineItemFixture {

    public static OrderLineItem OrderLineItem() {
        return new OrderLineItem(null, OrderMenu.of(1L, new Name("A"), new Price(BigDecimal.ONE)), new Quantity(1));
    }
}
