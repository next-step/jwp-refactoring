package kitchenpos.fixture;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;

import java.util.Arrays;
import java.util.List;

public class OrderFixture {
    public static Order 생성(OrderTable orderTable) {
        return new Order(orderTable);
    }

    public static OrderRequest request생성(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
        return new OrderRequest(orderTableId, orderLineItemRequests);
    }

    public static OrderRequest 샘플_Request_1L() {
        List<OrderLineItemRequest> orderLineItemRequests =
                Arrays.asList(OrderLineItemFixture.생성_Request(1L, 1L), OrderLineItemFixture.생성_Request(2L, 2L));
        return new OrderRequest(1L, orderLineItemRequests);
    }
    public static OrderRequest 샘플_Request_2L() {
        List<OrderLineItemRequest> orderLineItemRequests =
                Arrays.asList(OrderLineItemFixture.생성_Request(3L, 1L), OrderLineItemFixture.생성_Request(4L, 2L));
        return new OrderRequest(2L, orderLineItemRequests);
    }
}
