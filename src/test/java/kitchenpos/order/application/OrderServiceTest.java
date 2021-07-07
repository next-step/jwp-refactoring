package kitchenpos.order.application;

import kitchenpos.menu.dao.MenuDao;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.application.OrderService;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

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

    @DisplayName("주문 생성 테스트")
    @Test
    void createTest() {
        // given
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 3);
        OrderLineItem orderLineItem2 = new OrderLineItem(1L, 2L, 1);
        Order order = new Order(1L, null, Arrays.asList(orderLineItem1, orderLineItem2));
        OrderTable orderTable = new OrderTable(1L, 5);

        Mockito.when(menuDao.countByIdIn(any())).thenReturn(2L);
        Mockito.when(orderTableDao.findById(1L)).thenReturn(Optional.of(orderTable));
        Mockito.when(orderDao.save(any())).thenReturn(order);
        Mockito.when(orderLineItemDao.save(orderLineItem1)).thenReturn(orderLineItem1);
        Mockito.when(orderLineItemDao.save(orderLineItem2)).thenReturn(orderLineItem2);

        // when
        Order actual = orderService.create(order);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @DisplayName("주문할 메뉴가 비었을 경우 테스트")
    @Test
    void createTestMenuEmpty() {
        // given
        Order order = new Order(1L, null, Collections.emptyList());

        // when
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문할 메뉴가 없을 경우 테스트")
    @Test
    void createTestWithoutMenu() {
        // given
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 3);
        OrderLineItem orderLineItem2 = new OrderLineItem(1L, 2L, 1);
        Order order = new Order(1L, null, Arrays.asList(orderLineItem1, orderLineItem2));

        Mockito.when(menuDao.countByIdIn(any())).thenReturn(1L);

        // when
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 대상인 테이블이 없을 경우 테스트")
    @Test
    void createTestWithoutOrderTable() {
        // given
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 3);
        OrderLineItem orderLineItem2 = new OrderLineItem(1L, 2L, 1);
        Order order = new Order(1L, null, Arrays.asList(orderLineItem1, orderLineItem2));

        Mockito.when(menuDao.countByIdIn(any())).thenReturn(2L);
        Mockito.when(orderTableDao.findById(1L)).thenReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 주문 테이블 리스트 조회 테스트")
    @Test
    void listTest() {
        // given
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 3);
        OrderLineItem orderLineItem2 = new OrderLineItem(1L, 2L, 1);
        Order order1 = new Order(1L, null, Arrays.asList(orderLineItem1, orderLineItem2));
        Order order2 = new Order(1L, null, Arrays.asList(orderLineItem1, orderLineItem2));

        Mockito.when(orderDao.findAll()).thenReturn(Arrays.asList(order1, order2));
        Mockito.when(orderLineItemDao.findAllByOrderId(any())).thenReturn(Arrays.asList(orderLineItem1, orderLineItem2));

        // when
        List<Order> actual = orderService.list();

        // then
        assertThat(actual).isNotEmpty().hasSize(2);
    }

    @DisplayName("주문 상태 변경 테스트")
    @Test
    void changeOrderStatusTest() {
        // given
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 3);
        OrderLineItem orderLineItem2 = new OrderLineItem(1L, 2L, 1);
        Order order = new Order(1L, null, Arrays.asList(orderLineItem1, orderLineItem2));
        Order expected = new Order(1L, OrderStatus.COOKING.name(), Arrays.asList(orderLineItem1, orderLineItem2));

        Mockito.when(orderDao.findById(any())).thenReturn(Optional.of(order));
        Mockito.when(orderLineItemDao.findAllByOrderId(any())).thenReturn(Arrays.asList(orderLineItem1, orderLineItem2));

        // when
        Order actual = orderService.changeOrderStatus(1l, expected);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @DisplayName("주문 대상이 없는 경우 테스트")
    @Test
    void changeOrderStatusWithoutOrderTest() {
        // given
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 3);
        OrderLineItem orderLineItem2 = new OrderLineItem(1L, 2L, 1);
        Order expected = new Order(1L, OrderStatus.COOKING.name(), Arrays.asList(orderLineItem1, orderLineItem2));

        Mockito.when(orderDao.findById(any())).thenReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> orderService.changeOrderStatus(1l, expected))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문이 이미 완료가 된 경우 테스트")
    @Test
    void changeOrderStatusAlreadyCompleteTest() {
        // given
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 3);
        OrderLineItem orderLineItem2 = new OrderLineItem(1L, 2L, 1);
        Order order = new Order(1L, OrderStatus.COMPLETION.name(), Arrays.asList(orderLineItem1, orderLineItem2));
        Order expected = new Order(1L, OrderStatus.COOKING.name(), Arrays.asList(orderLineItem1, orderLineItem2));

        Mockito.when(orderDao.findById(any())).thenReturn(Optional.of(order));

        // when
        assertThatThrownBy(() -> orderService.changeOrderStatus(1l, expected))
            .isInstanceOf(IllegalArgumentException.class);
    }

}
