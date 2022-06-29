package kitchenpos.helper;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.consts.OrderStatus;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;

public class OrderFixtures {

    public static OrderRequest 주문_상태_조리_요청 = 주문_요청_만들기(OrderStatus.COMPLETION);
    public static OrderRequest 주문_상태_식사_요청 = 주문_요청_만들기(OrderStatus.MEAL);
    public static OrderRequest 주문_상태_계산완료_요청 = 주문_요청_만들기(OrderStatus.COMPLETION);

    public static OrderRequest 주문_요청_만들기(Long id,
                                         Long orderTableId,
                                         String orderStatus,
                                         List<OrderLineItemRequest> orderLineItems) {
        return new OrderRequest(id, orderTableId, orderStatus, orderLineItems);
    }


    public static OrderRequest 주문_요청_만들기(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        return 주문_요청_만들기(null, orderTableId, null, orderLineItems);
    }

    public static OrderRequest 주문_요청_만들기(OrderStatus orderStatus) {
        return 주문_요청_만들기(null, null, orderStatus.name(), null);
    }

    public static Order 주문_만들기(OrderStatus orderStatus, OrderTable orderTable, OrderLineItems orderLineItems) {
        return 주문_만들기(null, orderStatus, orderTable, orderLineItems);
    }

    public static Order 주문_만들기(Long id, OrderStatus orderStatus, OrderTable orderTable, OrderLineItems orderLineItems) {
        return new Order(id, orderStatus, LocalDateTime.now(), orderTable, orderLineItems);
    }
}
