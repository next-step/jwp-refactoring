package api.order;

import api.order.dto.OrderLineItemSaveRequest;
import api.order.dto.OrderSaveRequest;

import java.util.Arrays;
import java.util.List;

import static api.order.OrderLineItemFixtures.주문정보_1개_등록요청;

/**
 * packageName : kitchenpos.fixtures
 * fileName : OrderFixtures
 * author : haedoang
 * date : 2021/12/18
 * description :
 */
public class OrderFixtures {
    public static OrderSaveRequest 주문등록요청() {
        return OrderSaveRequest.of(1L, Arrays.asList(주문정보_1개_등록요청()));
    }

    public static OrderSaveRequest 주문등록요청(Long orderTableId, List<OrderLineItemSaveRequest> orderLineItemSaveRequests) {
        return OrderSaveRequest.of(orderTableId, orderLineItemSaveRequests);
    }
}
