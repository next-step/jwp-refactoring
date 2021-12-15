package kitchenpos.application;

import static kitchenpos.application.TableServiceTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(
            menuDao, orderDao, orderLineItemDao, orderTableDao);
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void createOrder() {
        // given
        Long orderId = 1L;
        OrderLineItem orderLineItem = 주문_항목_생성(1L, 1);
        OrderTable orderTable = 주문_테이블_생성(1L, 0, false);
        Order order = 주문_생성(orderId, orderTable.getId(), Collections.singletonList(orderLineItem));

        given(menuDao.countByIdIn(any())).willReturn((long)order.getOrderLineItems().size());
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));
        given(orderDao.save(any())).willReturn(order);
        given(orderLineItemDao.save(any())).willReturn(orderLineItem);

        // when
        Order savedOrder = orderService.create(order);

        // then
        assertEquals(OrderStatus.COOKING.name(), savedOrder.getOrderStatus());
        assertEquals(orderTable.getId(), savedOrder.getOrderTableId());
        assertThat(savedOrder.getOrderLineItems())
            .extracting("orderId").containsExactly(orderId);
    }

    @DisplayName("주문 항목이 존재 해야 주문을 등록할 수 있다.")
    @Test
    void createOrderEmptyOrderLineItems() {
        // given
        Long orderId = 1L;
        OrderTable orderTable = 주문_테이블_생성(1L, 0, false);
        Order order = 주문_생성(orderId, orderTable.getId(), Collections.emptyList());

        // when && then
        assertThrows(IllegalArgumentException.class, () -> orderService.create(order));
        verify(menuDao, times(0)).countByIdIn(any());
    }

    @DisplayName("주문 항목과 해당하는 메뉴의 숫자가 일치해야 한다.")
    @Test
    void createOrderNotMenuCount() {
        // given
        Long orderId = 1L;
        OrderLineItem orderLineItem = 주문_항목_생성(1L, 1);
        OrderTable orderTable = 주문_테이블_생성(1L, 0, false);
        Order order = 주문_생성(orderId, orderTable.getId(), Collections.singletonList(orderLineItem));

        given(menuDao.countByIdIn(any())).willReturn(0L);

        // when && then
        assertThrows(IllegalArgumentException.class, () -> orderService.create(order));
        verify(menuDao).countByIdIn(any());
        verify(orderTableDao, times(0)).findById(any());
    }

    @DisplayName("주문 테이블이 존재해야 주문을 등록할 수 있다.")
    @Test
    void createOrderNotFoundOrderTable() {
        // given
        Long orderId = 1L;
        OrderLineItem orderLineItem = 주문_항목_생성(1L, 1);
        Order order = 주문_생성(orderId, null, Collections.singletonList(orderLineItem));

        given(menuDao.countByIdIn(any())).willReturn(0L);

        // when && then
        assertThrows(IllegalArgumentException.class, () -> orderService.create(order));
        verify(orderDao, times(0)).save(any());
    }

    @DisplayName("주문 테이블이 빈 테이블이면 주문을 등록할 수 없다.")
    @Test
    void createOrderIsEmptyOrderTable() {
        // given
        Long orderId = 1L;
        OrderLineItem orderLineItem = 주문_항목_생성(1L, 1);
        OrderTable orderTable = 주문_테이블_생성(1L, 0, true);
        Order order = 주문_생성(orderId, orderTable.getId(), Collections.singletonList(orderLineItem));

        given(menuDao.countByIdIn(any())).willReturn((long)order.getOrderLineItems().size());
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));

        // when && then
        assertThrows(IllegalArgumentException.class, () -> orderService.create(order));
        verify(orderDao, times(0)).save(any());
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void getOrders() {
        // given
        Long orderId = 1L;
        OrderLineItem orderLineItem = 주문_항목_생성(1L, 1);
        List<OrderLineItem> orderLineItems = Collections.singletonList(orderLineItem);
        OrderTable orderTable = 주문_테이블_생성(1L, 0, false);
        Order order = 주문_생성(orderId, orderTable.getId(), orderLineItems);
        List<Order> orders = Collections.singletonList(order);

        given(orderDao.findAll()).willReturn(orders);
        given(orderLineItemDao.findAllByOrderId(orderId)).willReturn(orderLineItems);

        // when
        List<Order> findOrders = orderService.list();

        assertEquals(findOrders, orders);
    }

    @DisplayName("주문 상태를 갱신한다.")
    @Test
    void changeOrderStatus() {
        // given
        Long orderId = 1L;
        OrderLineItem orderLineItem = 주문_항목_생성(1L, 1);
        OrderTable orderTable = 주문_테이블_생성(1L, 0, false);
        Order order = 주문_생성(orderId, orderTable.getId(),
            OrderStatus.MEAL.name(), Collections.singletonList(orderLineItem));
        Order findOrder = 주문_생성(orderId, orderTable.getId(),
            OrderStatus.COOKING.name(), Collections.singletonList(orderLineItem));

        given(orderDao.findById(any())).willReturn(Optional.of(findOrder));
        given(orderDao.save(any())).willReturn(null);
        given(orderLineItemDao.findAllByOrderId(any()))
            .willReturn(Collections.singletonList(orderLineItem));

        // when
        Order savedOrder = orderService.changeOrderStatus(orderId, order);

        // then
        assertEquals(order.getOrderStatus(), savedOrder.getOrderStatus());
    }

    @DisplayName("주문 상태가 계산완료 상태면 주문 상태를 갱신할 수 없다.")
    @Test
    void changeOrderStatusIsCompletion() {
        // given
        Long orderId = 1L;
        OrderLineItem orderLineItem = 주문_항목_생성(1L, 1);
        OrderTable orderTable = 주문_테이블_생성(1L, 0, false);
        Order order = 주문_생성(orderId, orderTable.getId(),
            OrderStatus.COMPLETION.name(), Collections.singletonList(orderLineItem));

        given(orderDao.findById(any())).willReturn(Optional.of(order));

        // when && then
        assertThrows(IllegalArgumentException.class, () ->
            orderService.changeOrderStatus(orderId, order));
        verify(orderDao, times(0)).save(any());
    }

    private Order 주문_생성(Long orderId, Long orderTableId, List<OrderLineItem> orderLineItems) {
        return new Order(orderId, orderTableId, orderLineItems);
    }

    private Order 주문_생성(Long orderId, Long orderTableId, String orderStatus, List<OrderLineItem> orderLineItems) {
        return new Order(orderId, orderTableId, orderStatus, orderLineItems);
    }

    private OrderLineItem 주문_항목_생성(Long menuId, long quantity) {
        return new OrderLineItem(menuId, quantity);
    }
}
