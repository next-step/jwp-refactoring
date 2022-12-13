package kitchenpos.application;

import static kitchenpos.domain.OrderFixture.*;
import static kitchenpos.domain.OrderLineItemFixture.*;
import static kitchenpos.domain.OrderTableFixture.*;
import static org.assertj.core.api.Assertions.*;
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

import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;

@DisplayName("주문 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private OrderValidator orderValidator;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("주문 등록 API - 등록 되어 있지 않은 주문 테이블")
    @Test
    void create_order_table_not_exists() {
        // given
        Long orderTableId = 1L;
        Long menuId1 = 1L;
        Long menuId2 = 2L;
        Order order = orderParam(orderTableId, Arrays.asList(
            orderLineItemParam(menuId1, 1),
            orderLineItemParam(menuId2, 2))
        );
        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 API")
    @Test
    void create() {
        // given
        Long orderTableId = 1L;
        Long menuId1 = 1L;
        Long menuId2 = 2L;
        OrderLineItem orderLineItem1 = orderLineItemParam(menuId1, 1);
        OrderLineItem orderLineItem2 = orderLineItemParam(menuId2, 2);

        Order order = orderParam(orderTableId, Arrays.asList(
            orderLineItem1,
            orderLineItem2
        ));
        OrderLineItem savedOrderLineItem1 = savedOrderLineItem(1L);
        OrderLineItem savedOrderLineItem2 = savedOrderLineItem(2L);
        Order savedOrder = savedOrder(1L, orderTableId, Arrays.asList(savedOrderLineItem1, savedOrderLineItem2));

        long menuCount = 2L;
        given(menuRepository.countByIdIn(Arrays.asList(menuId1, menuId2))).willReturn(menuCount);
        OrderTable orderTable = savedOrderTable(orderTableId, false);
        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(orderTable));
        given(orderRepository.save(order)).willReturn(savedOrder);
        doNothing().when(orderValidator).validateSave(order, orderTable, menuCount);

        // when
        Order actual = orderService.create(order);

        // then
        assertThat(actual.getOrderTableId()).isEqualTo(orderTable.getId());
        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(actual.getOrderedTime()).isNotNull();
        assertThat(actual.getOrderLineItems()).containsExactly(savedOrderLineItem1, savedOrderLineItem2);
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
        Order order = orderParam(orderStatus);
        given(orderRepository.findById(orderId)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 수정 API - 이미 완료된 주문")
    @ParameterizedTest
    @EnumSource
    void changeOrderStatus_save_order_already_completion(OrderStatus orderStatus) {
        // given
        Long orderId = 1L;
        Order order = orderParam(orderStatus);
        given(orderRepository.findById(orderId)).willReturn(
            Optional.of(savedOrder(orderId, OrderStatus.COMPLETION)));

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
        Order order = orderParam(orderStatus);
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
