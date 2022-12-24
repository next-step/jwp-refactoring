package kitchenpos.order.domain.fixture;

import kitchenpos.common.vo.Name;
import kitchenpos.common.vo.Price;
import kitchenpos.common.vo.Quantity;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;

import java.math.BigDecimal;

public class OrderLineItemFixture {

    public static OrderLineItem OrderLineItem() {
        return new OrderLineItem(null, OrderMenu.of(1L, new Name("A"), new Price(BigDecimal.ONE)), new Quantity(1));
    }
}
