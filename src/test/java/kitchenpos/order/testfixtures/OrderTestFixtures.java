package kitchenpos.order.testfixtures;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;

public class OrderTestFixtures {

    public static void 특정_테이블이_특정_상태인지_조회_모킹(OrderRepository orderRepository, boolean isExist) {
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), anyList()))
            .willReturn(isExist);
    }

    public static void 특정_테이블들이_특정상태인지_조회_모킹(OrderRepository orderRepository, boolean isExist) {
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), anyList()))
            .willReturn(isExist);
    }

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
        return new OrderRequest(order.getOrderTable().getId(),
            convertToOrderLineItemRequests(order.getOrderLineItemList()));
    }

    public static List<OrderLineItemRequest> convertToOrderLineItemRequests(
        List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
            .map(orderLineItem -> convertToOrderLineItemRequest(orderLineItem))
            .collect(Collectors.toList());
    }

    private static OrderLineItemRequest convertToOrderLineItemRequest(OrderLineItem orderLineItem) {
        return new OrderLineItemRequest(orderLineItem.getMenu().getId(),
            orderLineItem.getQuantityVal());
    }
}
