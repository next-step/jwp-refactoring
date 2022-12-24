package kitchenpos.order.fixture;

import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.dto.OrderLineItemRequest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.menu.fixture.MenuProductTestFixture.짜장면메뉴상품요청;
import static kitchenpos.menu.fixture.MenuTestFixture.메뉴세트;

public class OrderLineItemTestFixture {

    public static OrderLineItemRequest 주문정보요청(Long menuId, long quantity) {
        return OrderLineItemRequest.of(menuId, quantity);
    }

    public static OrderLineItem 주문정보(Long menuId, long quantity) {
        return OrderLineItem.of(OrderMenu.from(메뉴세트(MenuRequest.of("메뉴", BigDecimal.ONE, 1L, Collections.singletonList(짜장면메뉴상품요청())), menuId)), quantity);
    }

    public static List<OrderLineItemRequest> 주문정보요청목록(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(orderLineItem -> OrderLineItemRequest.of(orderLineItem.getOrderMenu().getMenuId(), orderLineItem.getQuantity()))
                .collect(Collectors.toList());
    }

    public static List<OrderLineItem> 주문정보목록(List<OrderLineItemRequest> orderLineItems) {
        return orderLineItems.stream()
                .map(orderLineItem -> OrderLineItem.of(OrderMenu.from(메뉴세트(MenuRequest.of("메뉴", BigDecimal.ONE, 1L, Collections.singletonList(짜장면메뉴상품요청())), 1L)), 1L))
                .collect(Collectors.toList());
    }
}
