package kitchenpos.order.fixtures;

import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.OrderLineItemSaveRequest;
import kitchenpos.order.dto.OrderSaveRequest;
import kitchenpos.order.dto.OrderStatusUpdateRequest;
import kitchenpos.order.domain.OrderStatus;
import org.assertj.core.util.Lists;

import java.util.List;

import static kitchenpos.order.fixtures.OrderLineItemFixtures.주문정보_1개_등록요청;

/**
 * packageName : kitchenpos.fixtures
 * fileName : OrderFixtures
 * author : haedoang
 * date : 2021/12/18
 * description :
 */
public class OrderFixtures {
    public static OrderSaveRequest 주문등록요청() {
        return new OrderSaveRequest(1L, Lists.newArrayList(주문정보_1개_등록요청()));
    }

    public static OrderSaveRequest 주문등록요청(Long orderTableId, List<OrderLineItemSaveRequest> orderLineItemSaveRequests) {
        return new OrderSaveRequest(orderTableId, orderLineItemSaveRequests);
    }

    public static OrderStatusUpdateRequest 식사중으로_변경요청() {
        return new OrderStatusUpdateRequest(OrderStatus.MEAL);
    }

    public static OrderStatusUpdateRequest 식사완료로_변경요청() {
        return new OrderStatusUpdateRequest(OrderStatus.COMPLETION);
    }
}
