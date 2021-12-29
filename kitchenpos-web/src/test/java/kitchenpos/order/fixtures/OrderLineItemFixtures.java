package kitchenpos.order.fixtures;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemSaveRequest;

/**
 * packageName : kitchenpos.fixtures
 * fileName : OrderLineItemFixtures
 * author : haedoang
 * date : 2021/12/21
 * description :
 */
public class OrderLineItemFixtures {
    public static OrderLineItemSaveRequest 주문정보_1개_등록요청() {
        return OrderLineItemSaveRequest.of(1L, 1L);
    }

    public static OrderLineItemSaveRequest 주문정보_등록요청(Long menuId, Long quantity) {
        return OrderLineItemSaveRequest.of(menuId, quantity);
    }
    
    public static OrderLineItem 주문정보() {
        return new OrderLineItem(1L, 1L);
    }
}
