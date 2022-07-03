package kitchenpos.fixture;

import java.util.List;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;

public class OrderFixtureFactory {
    private OrderFixtureFactory() {
    }

    public static OrderRequest createOrder(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
        return new OrderRequest(orderTableId, orderLineItemRequests);
    }

    public static OrderRequest createParamForUpdateStatus(OrderStatus status) {
        return new OrderRequest(status.name());
    }
}
