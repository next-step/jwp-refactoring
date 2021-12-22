package kitchenpos.order.fixtures;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemSaveRequest;

import java.math.BigDecimal;

import static java.util.Collections.singletonList;
import static kitchenpos.menu.fixtures.MenuFixtures.메뉴;
import static kitchenpos.menu.fixtures.MenuGroupFixtures.메뉴그룹;
import static kitchenpos.menu.fixtures.MenuProductFixtures.메뉴상품;
import static kitchenpos.menu.fixtures.ProductFixtures.양념치킨;

/**
 * packageName : kitchenpos.fixtures
 * fileName : OrderLineItemFixtures
 * author : haedoang
 * date : 2021/12/21
 * description :
 */
public class OrderLineItemFixtures {
    public static OrderLineItemSaveRequest 주문정보_1개_등록요청() {
        return new OrderLineItemSaveRequest(1L, 1L);
    }

    public static OrderLineItemSaveRequest 주문정보_등록요청(Long menuId, Long quantity) {
        return new OrderLineItemSaveRequest(menuId, quantity);
    }

    public static OrderLineItem 주문정보() {
        return new OrderLineItem(메뉴("메뉴", new BigDecimal(16000), 메뉴그룹("메뉴그룹"), singletonList(메뉴상품(양념치킨(), 1L))), 1L);
    }
}
