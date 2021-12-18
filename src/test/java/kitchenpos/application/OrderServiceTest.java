package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

    @DisplayName("주문을 생성한다")
    @Test
    void creteTest() {
        OrderTable orderTable = new OrderTable(1L, 1L, 1, false);
        OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1L);
        Order order = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), Collections.singletonList(orderLineItem));
        when(menuDao.countByIdIn(Collections.singletonList(1L))).thenReturn(1L);
        when(orderTableDao.findById(1L)).thenReturn(java.util.Optional.of(orderTable));
        when(orderDao.save(order)).thenReturn(order);

        // when
        OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
        Order returnedOrder = orderService.create(order);

        assertThat(order).isEqualTo(returnedOrder);
    }

    @DisplayName("주문항목 없이 주문을 생성한다")
    @Test
    void creteWithoutOrderLineItemTest() {
        Order order = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), null);

        // when
        OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

        // then
        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 없이 주문을 생성한다")
    @Test
    void creteWithoutOrderTableTest() {
        OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1L);
        Order order = new Order(1L, null, OrderStatus.COOKING.name(), LocalDateTime.now(), Collections.singletonList(orderLineItem));

        // when
        OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

        // then
        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문목록을 조회할 수 있다")
    @Test
    void listTest() {
        OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1L);
        List<OrderLineItem> orderLineItems = Collections.singletonList(orderLineItem);
        Order order = new Order(1L, null, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
        List<Order> orders = Collections.singletonList(order);
        when(orderDao.findAll()).thenReturn(orders);
        when(orderLineItemDao.findAllByOrderId(order.getId())).thenReturn(orderLineItems);

        // when
        OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
        List<Order> list = orderService.list();

        // then
        assertThat(list).isEqualTo(orders);
    }

    @DisplayName("주문상태를 변경한다")
    @Test
    void changeStatus() {
        OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1L);
        Order order = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), Collections.singletonList(orderLineItem));
        Order orderToChangeStatus = new Order(1L, 1L, OrderStatus.MEAL.name(), LocalDateTime.now(), Collections.singletonList(orderLineItem));
        when(orderDao.findById(1L)).thenReturn(java.util.Optional.of(orderToChangeStatus));

        OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
        Order changedOrder = orderService.changeOrderStatus(order.getId(), orderToChangeStatus);

        assertThat(changedOrder).isEqualTo(orderToChangeStatus);
    }

    @DisplayName("결제완료 상태인 주문상태를 변경한다")
    @Test
    void changeCompleteStatus() {
        OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1L);
        Order order = new Order(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), Collections.singletonList(orderLineItem));
        Order orderToChangeStatus = new Order(1L, 1L, OrderStatus.MEAL.name(), LocalDateTime.now(), Collections.singletonList(orderLineItem));
        when(orderDao.findById(1L)).thenReturn(java.util.Optional.of(order));

        OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), orderToChangeStatus)).isInstanceOf(IllegalArgumentException.class);
    }
}
