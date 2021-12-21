package kitchenpos.fixtures;

import kitchenpos.dto.OrderLineItemResponse;
import kitchenpos.dto.OrderLineItemSaveRequest;
import org.aspectj.weaver.ast.Or;

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

    public static OrderLineItemSaveRequest 주문정보_2개_등록요청() {
        return new OrderLineItemSaveRequest(1L, 2L);
    }

    public static OrderLineItemSaveRequest 주문정보_등록요청(Long menuId, Long quantity) {
        return new OrderLineItemSaveRequest(menuId, quantity);
    }
}
