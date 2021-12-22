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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

    @InjectMocks
    OrderService orderService;

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        // given
        OrderLineItem orderLineItem = OrderLineItem.of(1L, 1L, 1L, 1);
        OrderTable orderTable = OrderTable.of(1L, 1L, 2, false);
        Order order = Order.of(1L, 1L, null, null, Arrays.asList(orderLineItem));
        when(menuDao.countByIdIn(Arrays.asList(1L))).thenReturn(Long.valueOf(1));
        when(orderTableDao.findById(1L)).thenReturn(Optional.of(orderTable));
        when(orderDao.save(order)).thenReturn(order);
        when(orderLineItemDao.save(orderLineItem)).thenReturn(orderLineItem);

        //when
        Order expected = orderService.create(order);

        //then
        assertThat(order.getId()).isEqualTo(expected.getId());
    }

    @DisplayName("주문 항목이 없는 주문을 생성한다.")
    @Test
    void create2() {
        // given
        Order order = Order.of(1L, 1L, null, null, null);

        //then
        assertThatThrownBy(
                () -> orderService.create(order)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴와 다른 주문 항목이 있는 주문을 생성한다.")
    @Test
    void create3() {
        // given
        OrderLineItem orderLineItem = OrderLineItem.of(1L, 1L, 1L, 1);
        Order order = Order.of(1L, 1L, null, null, Arrays.asList(orderLineItem));
        when(menuDao.countByIdIn(Arrays.asList(1L))).thenReturn(Long.valueOf(0));

        //then
        assertThatThrownBy(
                () -> orderService.create(order)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블 주문을 생성한다.")
    @Test
    void create4() {
        // given
        OrderLineItem orderLineItem = OrderLineItem.of(1L, 1L, 1L, 1);
        OrderTable orderTable = OrderTable.of(1L, 1L, 2, true);
        Order order = Order.of(1L, 1L, null, null, Arrays.asList(orderLineItem));
        when(menuDao.countByIdIn(Arrays.asList(1L))).thenReturn(Long.valueOf(1));
        when(orderTableDao.findById(1L)).thenReturn(Optional.of(orderTable));

        //then
        assertThatThrownBy(
                () -> orderService.create(order)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() {
        // given
        OrderLineItem orderLineItem = OrderLineItem.of(1L, 1L, 1L, 1);
        List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem);
        Order order = Order.of(1L, 1L, null, null, orderLineItems);
        Order order2 = Order.of(2L, 2L, null, null, orderLineItems);
        List<Order> actual = Arrays.asList(order, order2);
        when(orderDao.findAll()).thenReturn(actual);
        when(orderLineItemDao.findAllByOrderId(any())).thenReturn(orderLineItems);

        //when
        List<Order> expected = orderService.list();

        //then
        assertThat(actual.size()).isEqualTo(expected.size());
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // given
        OrderLineItem orderLineItem = OrderLineItem.of(1L, 1L, 1L, 1);
        Order order = Order.of(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), Arrays.asList(orderLineItem));
        Order targetOrder = Order.of(order.getId(), order.getOrderTableId(), OrderStatus.MEAL.name(), order.getOrderedTime(), order.getOrderLineItems());
        when(orderDao.findById(1L)).thenReturn(Optional.of(order));
        when(orderDao.save(order)).thenReturn(targetOrder);

        //when
        Order expected = orderService.changeOrderStatus(order.getId(), targetOrder);

        //then
        assertThat(OrderStatus.MEAL.name()).isEqualTo(expected.getOrderStatus());
    }

    @DisplayName("계산 완료인 주문 상태를 변경한다.")
    @Test
    void changeOrderStatus2() {
        // given
        OrderLineItem orderLineItem = OrderLineItem.of(1L, 1L, 1L, 1);
        Order order = Order.of(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), Arrays.asList(orderLineItem));
        Order targetOrder = Order.of(order.getId(), order.getOrderTableId(), OrderStatus.MEAL.name(), order.getOrderedTime(), order.getOrderLineItems());
        when(orderDao.findById(1L)).thenReturn(Optional.of(order));

        //then
        assertThatThrownBy(
                () -> orderService.changeOrderStatus(order.getId(), targetOrder)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
