package kitchenpos.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemFixture {
    public static OrderLineItem 생성(Long menuId, long quantity) {
        return new OrderLineItem(menuId, quantity);
    }

    public static OrderLineItem 샘플(){
        return null;
    }



}
