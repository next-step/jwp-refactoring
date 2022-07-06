package kitchenpos.order.application.behavior;

import java.util.List;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.application.fixture.OrderDtoFixtureFactory;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderContextServiceBehavior {

    @Autowired
    private OrderService orderService;

    public OrderResponse 주문_생성됨(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
        return orderService.create(OrderDtoFixtureFactory.createOrder(orderTableId, orderLineItemRequests));
    }

    public OrderResponse 주문상태_변경(Long orderId, OrderStatus orderStatus) {
        OrderRequest param = OrderDtoFixtureFactory.createParamForUpdateStatus(orderStatus);
        return orderService.changeOrderStatus(orderId, param);
    }
}
