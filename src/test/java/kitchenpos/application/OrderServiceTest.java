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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    MenuDao menuDao;
    @Mock
    OrderDao orderDao;
    @Mock
    OrderLineItemDao orderLineItemDao;
    @Mock
    OrderTableDao orderTableDao;

    @DisplayName("주문을 등록한다.")
    @Test
    void createTest(){
        // given
        Order order = mock(Order.class);

        OrderLineItem orderLineItem = mock(OrderLineItem.class);
        when(orderLineItem.getMenuId()).thenReturn(1L);
        List<Long> menuIds = new ArrayList<>();
        menuIds.add(orderLineItem.getMenuId());

        when(menuDao.countByIdIn(menuIds)).thenReturn(1L);
        when(order.getOrderLineItems()).thenReturn(Arrays.asList(orderLineItem));

        OrderTable orderTable = mock(OrderTable.class);
        when(orderTable.getId()).thenReturn(1L);
        when(orderTableDao.findById(order.getId())).thenReturn(Optional.of(orderTable));

        Order savedOrder = mock(Order.class);
        when(savedOrder.getId()).thenReturn(1L);
        when(orderDao.save(any())).thenReturn(savedOrder);
        OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

        // when
        Order createdOrder = orderService.create(order);

        // then
        assertThat(createdOrder.getId()).isNotNull();
    }

    @DisplayName("주문의 목록을 조회한다.")
    @Test
    void list(){

        Order order = mock(Order.class);
        when(order.getId()).thenReturn(1L);
        when(orderDao.findAll()).thenReturn(Arrays.asList(order));

        OrderLineItem orderLineItem = mock(OrderLineItem.class);
        when(orderLineItemDao.findAllByOrderId(order.getId())).thenReturn(Arrays.asList(orderLineItem));
        OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders).contains(order);

    }
  
    @DisplayName("주문의 상태를 변경한다.")
    @Test
    void changeOrderStatusTest() {

        // given
        Order order = new Order();
        order.setOrderStatus(OrderStatus.COOKING.toString());

        Order expectedOrder = mock(Order.class);
        when(expectedOrder.getId()).thenReturn(1L);
        when(expectedOrder.getOrderStatus()).thenReturn(OrderStatus.COOKING.toString());

        when(orderDao.findById(1L)).thenReturn(Optional.of(expectedOrder));

        OrderLineItem orderLineItem = mock(OrderLineItem.class);
        when(orderLineItemDao.findAllByOrderId(1L)).thenReturn(Arrays.asList(orderLineItem));

        OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
        // when
        Order savedOrder = orderService.changeOrderStatus(1L, order);
        // then
        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getOrderStatus()).isEqualTo(order.getOrderStatus());

    }

}
