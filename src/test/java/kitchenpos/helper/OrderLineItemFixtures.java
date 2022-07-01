package kitchenpos.helper;

import static kitchenpos.helper.MenuFixtures.메뉴_만들기;

import java.util.Arrays;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.dto.OrderLineItemRequest;

public class OrderLineItemFixtures {

    public static final OrderLineItemRequest 주문_항목_요청1 = 주문_항목_요청_만들기(1L, 1);
    public static final OrderLineItemRequest 주문_항목_요청2 = 주문_항목_요청_만들기(2L, 2);
    public static final OrderLineItemRequest 없는_주문_항목_요청 = 주문_항목_요청_만들기(9999999L, 2);

    public static OrderLineItemRequest 주문_항목_요청_만들기(Long menuId, Integer quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }

    public static OrderLineItem 주문_항목_만들기(Menu menu, Integer quantity) {
        return new OrderLineItem(OrderMenu.of(menu, new Quantity(quantity)));
    }

    public static OrderLineItems 주문_항목들_만들기() {
        return new OrderLineItems(Arrays.asList(주문_항목_만들기(메뉴_만들기(1L, "메뉴 만들기", 30000), 3)));
    }
}
