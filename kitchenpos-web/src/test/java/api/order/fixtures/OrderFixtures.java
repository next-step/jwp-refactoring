package api.order.fixtures;

import api.order.domain.Order;
import api.order.dto.OrderLineItemSaveRequest;
import api.order.dto.OrderSaveRequest;
import api.order.dto.OrderStatusUpdateRequest;
import api.order.domain.OrderStatus;
import org.assertj.core.util.Lists;

import java.util.Arrays;
import java.util.List;

import static api.order.fixtures.OrderLineItemFixtures.주문정보;
import static api.order.fixtures.OrderLineItemFixtures.주문정보_1개_등록요청;

/**
 * packageName : kitchenpos.fixtures
 * fileName : OrderFixtures
 * author : haedoang
 * date : 2021/12/18
 * description :
 */
public class OrderFixtures {
    public static OrderSaveRequest 주문등록요청() {
        return OrderSaveRequest.of(1L, Lists.newArrayList(주문정보_1개_등록요청()));
    }

    public static OrderSaveRequest 주문등록요청(Long orderTableId, List<OrderLineItemSaveRequest> orderLineItemSaveRequests) {
        return OrderSaveRequest.of(orderTableId, orderLineItemSaveRequests);
    }

    public static Order 주문() {
        return new Order(1L, Arrays.asList(주문정보(), 주문정보()));
    }

    public static OrderStatusUpdateRequest 식사중으로_변경요청() {
        return OrderStatusUpdateRequest.of(OrderStatus.MEAL);
    }

    public static OrderStatusUpdateRequest 식사완료로_변경요청() {
        return OrderStatusUpdateRequest.of(OrderStatus.COMPLETION);
    }
}
