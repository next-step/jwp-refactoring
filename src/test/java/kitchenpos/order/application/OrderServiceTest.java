package kitchenpos.order.application;

import static kitchenpos.order.domain.OrderFixture.*;
import static kitchenpos.order.domain.OrderLineItemFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;

@DisplayName("주문 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderValidator orderValidator;
    @Mock
    private OrderLineItemGenerator orderLineItemGenerator;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("주문 등록 API")
    @Test
    void create() {
        // given
        Long orderTableId = 1L;
        OrderRequest orderRequest = orderRequest(orderTableId, Collections.singletonList(orderLineItemRequest(1L, 1)));
        OrderLineItem savedOrderLineItem = savedOrderLineItem(1L, 1L, 5);
        Order savedOrder = savedOrder(1L, orderTableId, Collections.singletonList(savedOrderLineItem));

        given(orderRepository.save(any(Order.class))).willReturn(savedOrder);
        doNothing().when(orderValidator).validateSave(orderRequest);
        given(orderLineItemGenerator.generate(anyList())).willReturn(Collections.singletonList(savedOrderLineItem));

        // when
        OrderResponse actual = orderService.create(orderRequest);

        // then
        assertAll(
            () -> assertThat(actual.getOrderTableId()).isEqualTo(orderTableId),
            () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
            () -> assertThat(actual.getOrderedTime()).isNotNull(),
            () -> assertThat(actual.getOrderLineItems()).hasSize(1),
            () -> assertThat(actual.getOrderLineItems().get(0).getSeq()).isNotNull(),
            () -> assertThat(actual.getOrderLineItems().get(0).getOrderMenuId()).isEqualTo(1L),
            () -> assertThat(actual.getOrderLineItems().get(0).getQuantity()).isEqualTo(5)
        );
    }

    @DisplayName("주문 목록 조회 API")
    @Test
    void list() {
        // given
        OrderLineItem savedOrderLineItem1 = savedOrderLineItem(1L);
        OrderLineItem savedOrderLineItem2 = savedOrderLineItem(2L);
        Order savedOrder = savedOrder(1L, OrderStatus.COOKING, Arrays.asList(savedOrderLineItem1, savedOrderLineItem2));
        given(orderRepository.findAll()).willReturn(Collections.singletonList(savedOrder));

        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders).containsExactly(savedOrder);
        assertThat(orders.get(0).getOrderLineItems()).containsExactly(savedOrderLineItem1, savedOrderLineItem2);
    }

    @DisplayName("주문 수정 API - 저장된 주문 존재 하지 않음")
    @ParameterizedTest
    @EnumSource
    void changeOrderStatus_save_order_not_exists(OrderStatus orderStatus) {
        // given
        Long orderId = 1L;
        Order order = orderRequest(orderStatus);
        given(orderRepository.findById(orderId)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 수정 API")
    @ParameterizedTest
    @EnumSource
    void changeOrderStatus(OrderStatus orderStatus) {
        // given
        Long orderId = 1L;
        Order order = orderRequest(orderStatus);
        List<OrderLineItem> savedOrderLineItems = Collections.singletonList(savedOrderLineItem(1L));
        Order savedOrder = savedOrder(orderId, OrderStatus.COOKING, savedOrderLineItems);
        given(orderRepository.findById(orderId)).willReturn(Optional.of(savedOrder));

        // when
        Order actual = orderService.changeOrderStatus(orderId, order);

        // then
        assertThat(actual.getOrderStatus()).isEqualTo(orderStatus);
        assertThat(actual.getOrderLineItems()).isEqualTo(savedOrderLineItems);
    }
}
