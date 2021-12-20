package kitchenpos.fixture;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;

import java.util.List;

public class OrderFixture {
    public static Order 생성(OrderTable orderTable){
        return Order.of(orderTable);
    }
    public static OrderRequest request생성(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests){
        return new OrderRequest(orderTableId, orderLineItemRequests);
    }
}
