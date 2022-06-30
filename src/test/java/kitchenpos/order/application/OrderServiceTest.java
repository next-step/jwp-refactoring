package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderUpdateRequest;
import kitchenpos.order.infrastructure.OrderRepository;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderValidator orderValidator;

    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("주문을 생성한다.")
    void create() {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, "양념치킨", 19_000L, 1L);
        OrderRequest orderRequest = new OrderRequest(1L, Lists.list(orderLineItemRequest));
        Order order = orderRequest.toEntity();
        when(orderRepository.save(any())).thenReturn(order);

        OrderResponse saved = orderService.create(orderRequest);

        assertThat(saved.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    @DisplayName("주문 목록을 조회한다.")
    void findAll() {
        OrderLineItem orderLineItem = OrderLineItem.createOrderLineItem(1L, "양념치킨", 19_000L, 1L);
        OrderLineItems orderLineItems = OrderLineItems.createOrderLineItems(Lists.list(orderLineItem));
        Order order = Order.createOrder(1L, orderLineItems);
        when(orderRepository.findAll()).thenReturn(Lists.list(order));

        List<OrderResponse> actual = orderService.list();

        assertThat(actual).hasSize(1);
    }

    @Test
    @DisplayName("주문이 존재하지 않으면 주문 상태를 변경할 수 없다.")
    void changeOrderStatusWithNoExistingOrder() {
        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(99L, new OrderUpdateRequest());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문상태가 계산완료인경우, 주문 상태를 변경할 수 없다.")
    void changeOrderStatusWithCompletion() {
        OrderLineItem orderLineItem = OrderLineItem.createOrderLineItem(1L, "양념치킨", 19_000L, 1L);
        OrderLineItems orderLineItems = OrderLineItems.createOrderLineItems(Lists.list(orderLineItem));
        Order order = Order.createOrder(1L, orderLineItems);
        order.updateOrderStatus(OrderStatus.COMPLETION);
        when(orderRepository.findById(any())).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(1L, new OrderUpdateRequest());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 상태를 변경한다.")
    void changeOrderStatus() {
        OrderUpdateRequest orderUpdateRequest = new OrderUpdateRequest(OrderStatus.MEAL);
        OrderLineItem orderLineItem = OrderLineItem.createOrderLineItem(1L, "양념치킨", 19_000L, 1L);
        OrderLineItems orderLineItems = OrderLineItems.createOrderLineItems(Lists.list(orderLineItem));
        Order order = Order.createOrder(1L, orderLineItems);
        when(orderRepository.findById(any())).thenReturn(Optional.of(order));
        when(orderRepository.save(any())).thenReturn(order);

        OrderResponse changed = orderService.changeOrderStatus(1L, orderUpdateRequest);

        assertThat(changed.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }
}
