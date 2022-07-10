package kitchenpos.order.generator;

import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderLineItemRequest;

import java.util.List;

public class OrderGenerator {

    public static OrderCreateRequest 주문_생성_요청(Long orderTable, List<OrderLineItemRequest> orderLineItems) {
        return new OrderCreateRequest(orderTable, orderLineItems);
    }

    public static OrderLineItemRequest 주문_물품_생성_요청(Long menu, Long quantity) {
         return new OrderLineItemRequest(menu, quantity);
    }
}
