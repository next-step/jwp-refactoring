package kitchenpos.fixtures;

import kitchenpos.dto.OrderLineItemSaveRequest;

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
}
