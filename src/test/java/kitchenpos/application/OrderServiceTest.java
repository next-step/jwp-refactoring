package kitchenpos.application;

import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderLineItemRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.*;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderLineItemRepository orderLineItemRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @DisplayName("주문을 생성한다")
    @Test
    void creteTest() {
        OrderTable orderTable = new OrderTable(1L, null, 1, false);
        Order order = new Order(1L, orderTable, OrderStatus.COOKING.name(), LocalDateTime.now(), null);
        OrderLineItem orderLineItem = new OrderLineItem(1L, order, null, 1L);
        order.setOrderLineItems(new OrderLineItems(Collections.singletonList(orderLineItem)));
        when(menuRepository.countByIdIn(Collections.singletonList(1L))).thenReturn(1L);
        when(orderTableRepository.findById(1L)).thenReturn(java.util.Optional.of(orderTable));
        when(orderRepository.save(order)).thenReturn(order);

        // when
        OrderService orderService = new OrderService(menuRepository, orderRepository, orderLineItemRepository, orderTableRepository, menuService);
        Order returnedOrder = orderService.create(order);

        assertThat(order).isEqualTo(returnedOrder);
    }

    @DisplayName("주문항목 없이 주문을 생성한다")
    @Test
    void creteWithoutOrderLineItemTest() {
        OrderTable orderTable = new OrderTable();
        Order order = new Order(1L, orderTable, OrderStatus.COOKING.name(), LocalDateTime.now(), null);

        // when
        OrderService orderService = new OrderService(menuRepository, orderRepository, orderLineItemRepository, orderTableRepository, menuService);

        // then
        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 없이 주문을 생성한다")
    @Test
    void creteWithoutOrderTableTest() {
        Order order = new Order(1L, null, OrderStatus.COOKING.name(), LocalDateTime.now(), null);
        OrderLineItem orderLineItem = new OrderLineItem(1L, order, 1L, 1L);
        order.setOrderLineItems(new OrderLineItems(Collections.singletonList(orderLineItem)));

        // when
        OrderService orderService = new OrderService(menuRepository, orderRepository, orderLineItemRepository, orderTableRepository, menuService);

        // then
        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문목록을 조회할 수 있다")
    @Test
    void listTest() {
        Order order = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), null);
        OrderLineItem orderLineItem = new OrderLineItem(1L, order, 1L, 1L);
        order.setOrderLineItems(new OrderLineItems(Collections.singletonList(orderLineItem)));
        List<OrderLineItem> orderLineItems = Collections.singletonList(orderLineItem);
        List<Order> orders = Collections.singletonList(order);
        when(orderRepository.findAll()).thenReturn(orders);
        when(orderLineItemRepository.findAllByOrderId(order.getId())).thenReturn(orderLineItems);

        // when
        OrderService orderService = new OrderService(menuRepository, orderRepository, orderLineItemRepository, orderTableRepository, menuService);
        List<Order> list = orderService.list();

        // then
        assertThat(list).isEqualTo(orders);
    }

    @DisplayName("주문상태를 변경한다")
    @Test
    void changeStatus() {
        Order order = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), null);
        OrderLineItem orderLineItem = new OrderLineItem(1L, order, 1L, 1L);
        order.setOrderLineItems(new OrderLineItems(Collections.singletonList(orderLineItem)));
        Order orderToChangeStatus = new Order(1L, 1L, OrderStatus.MEAL.name(), LocalDateTime.now(), new OrderLineItems(Collections.singletonList(orderLineItem)));
        when(orderRepository.findById(1L)).thenReturn(java.util.Optional.of(orderToChangeStatus));

        OrderService orderService = new OrderService(menuRepository, orderRepository, orderLineItemRepository, orderTableRepository, menuService);
        Order changedOrder = orderService.changeOrderStatus(order.getId(), orderToChangeStatus);

        assertThat(changedOrder).isEqualTo(orderToChangeStatus);
    }

    @DisplayName("결제완료 상태인 주문상태를 변경한다")
    @Test
    void changeCompleteStatus() {
        Order order = new Order(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), null);
        OrderLineItem orderLineItem = new OrderLineItem(1L, order, 1L, 1L);
        OrderLineItems orderLineItems = new OrderLineItems(Collections.singletonList(orderLineItem));
        order.setOrderLineItems(orderLineItems);
        Order orderToChangeStatus = new Order(1L, 1L, OrderStatus.MEAL.name(), LocalDateTime.now(), orderLineItems);
        when(orderRepository.findById(1L)).thenReturn(java.util.Optional.of(order));

        OrderService orderService = new OrderService(menuRepository, orderRepository, orderLineItemRepository, orderTableRepository, menuService);

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), orderToChangeStatus)).isInstanceOf(IllegalArgumentException.class);
    }
}
