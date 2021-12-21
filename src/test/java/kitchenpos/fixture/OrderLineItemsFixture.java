package kitchenpos.fixture;

import kitchenpos.domain.OrderLineItem;
import org.assertj.core.util.Lists;

import java.util.List;

import static kitchenpos.fixture.MenuFixture.menu1;
import static kitchenpos.fixture.OrderFixture.order;

public class OrderLineItemsFixture {
    public static final List<OrderLineItem> orderLineItems = Lists.newArrayList(OrderLineItem.of(1L, order.getId(), menu1.getId(), 1L));;
}
