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

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@DisplayName("주문 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private MenuDao menuDao;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderLineItemDao orderLineItemDao;
    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("주문 등록 API - 빈 주문 항목")
    @Test
    void create_order_line_items_is_empty() {
        // given
        Order order = orderParam(1L, Collections.emptyList());

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 API - 등록 되어 있지 않은 주문 항목 메뉴")
    @Test
    void create_order_line_items_not_exists() {
        // given
        Long menuId1 = 1L;
        Long menuId2 = 2L;
        Order order = orderParam(1L, Arrays.asList(
            orderLineItemParam(menuId1, 1),
            orderLineItemParam(menuId2, 2))
        );
        given(menuDao.countByIdIn(Arrays.asList(menuId1, menuId2))).willReturn(1L);

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

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
        given(menuDao.countByIdIn(Arrays.asList(menuId1, menuId2))).willReturn(2L);
        given(orderTableDao.findById(orderTableId)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 API - 주문 테이블 빈 테이블")
    @Test
    void create_order_table_is_empty() {
        // given
        Long orderTableId = 1L;
        Long menuId1 = 1L;
        Long menuId2 = 2L;
        Order order = orderParam(orderTableId, Arrays.asList(
            orderLineItemParam(menuId1, 1),
            orderLineItemParam(menuId2, 2))
        );
        given(menuDao.countByIdIn(Arrays.asList(menuId1, menuId2))).willReturn(2L);
        OrderTable orderTable = savedOrderTable(orderTableId, true);
        given(orderTableDao.findById(orderTableId)).willReturn(Optional.of(orderTable));

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

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
            orderLineItem2)
        );
        Order savedOrder = savedOrder(1L, orderTableId);
        OrderLineItem savedOrderLineItem1 = savedOrderLineItem(1L, savedOrder.getId());
        OrderLineItem savedOrderLineItem2 = savedOrderLineItem(2L, savedOrder.getId());

        given(menuDao.countByIdIn(Arrays.asList(menuId1, menuId2))).willReturn(2L);
        OrderTable orderTable = savedOrderTable(orderTableId, false);
        given(orderTableDao.findById(orderTableId)).willReturn(Optional.of(orderTable));
        given(orderDao.save(order)).willReturn(savedOrder);
        given(orderLineItemDao.save(orderLineItem1)).willReturn(savedOrderLineItem1);
        given(orderLineItemDao.save(orderLineItem2)).willReturn(savedOrderLineItem2);

        // when
        Order actual = orderService.create(order);

        // then
        assertThat(actual.getOrderTableId()).isEqualTo(orderTable.getId());
        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(actual.getOrderedTime()).isNotNull();
        assertThat(actual.getOrderLineItems()).containsExactly(savedOrderLineItem1, savedOrderLineItem2);
    }

    @DisplayName("주문 목록 조회 API")
    @Test
    void list() {
        // given
        Long orderId = 1L;
        Order savedOrder = savedOrder(orderId, "COOKING");
        OrderLineItem savedOrderLineItem1 = savedOrderLineItem(1L, savedOrder.getId());
        OrderLineItem savedOrderLineItem2 = savedOrderLineItem(2L, savedOrder.getId());
        given(orderDao.findAll()).willReturn(Collections.singletonList(savedOrder));
        given(orderLineItemDao.findAllByOrderId(orderId))
            .willReturn(Arrays.asList(savedOrderLineItem1, savedOrderLineItem2));

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
        Order order = orderParam(orderStatus.name());
        given(orderDao.findById(orderId)).willReturn(Optional.empty());

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
        Order order = orderParam(orderStatus.name());
        given(orderDao.findById(orderId)).willReturn(Optional.of(savedOrder(orderId, OrderStatus.COMPLETION.name())));

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
        Order order = orderParam(orderStatus.name());
        Order savedOrder = savedOrder(orderId, OrderStatus.COOKING.name());
        List<OrderLineItem> savedOrderLineItems = Collections.singletonList(savedOrderLineItem(1L, orderId));
        given(orderDao.findById(orderId)).willReturn(Optional.of(savedOrder));
        given(orderDao.save(savedOrder)).willReturn(savedOrder);
        given(orderLineItemDao.findAllByOrderId(orderId))
            .willReturn(savedOrderLineItems);

        // when
        Order actual = orderService.changeOrderStatus(orderId, order);

        // then
        assertThat(actual.getOrderStatus()).isEqualTo(orderStatus.name());
        assertThat(actual.getOrderLineItems()).isEqualTo(savedOrderLineItems);
    }
}
