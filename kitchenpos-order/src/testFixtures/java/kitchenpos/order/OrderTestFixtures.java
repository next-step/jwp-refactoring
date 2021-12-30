package kitchenpos.order;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderTestFixtures {

    public static void 주문_저장_결과_모킹(OrderRepository orderRepository, Order order) {
        given(orderRepository.save(any()))
            .willReturn(order);
    }

    public static void 주문_전체_조회_모킹(OrderRepository orderRepository, List<Order> orders) {
        given(orderRepository.findAll())
            .willReturn(orders);
    }

    public static void 특정_주문_조회_모킹(OrderRepository orderRepository, Order order) {
        given(orderRepository.findById(any()))
            .willReturn(Optional.of(order));
    }

    public static OrderRequest convertToOrderRequest(Order order) {
        return new OrderRequest(order.getOrderTableId(),
            convertToOrderLineItemRequests(order.getOrderLineItemList()));
    }

    public static OrderRequest convertToChangeOrderStatusRequest(OrderStatus orderStatus) {
        return new OrderRequest(orderStatus);
    }

    public static List<OrderLineItemRequest> convertToOrderLineItemRequests(
        List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
            .map(orderLineItem -> convertToOrderLineItemRequest(orderLineItem))
            .collect(Collectors.toList());
    }

    private static OrderLineItemRequest convertToOrderLineItemRequest(OrderLineItem orderLineItem) {
        return new OrderLineItemRequest(orderLineItem.getMenuId(),
            orderLineItem.getQuantityVal());
    }
}
