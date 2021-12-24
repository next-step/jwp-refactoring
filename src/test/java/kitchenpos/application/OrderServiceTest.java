package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
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

    private OrderLineItem orderLineItem;

    private OrderTable orderTable;

    private Order order;

    private Order order2;

    @BeforeEach
    void setUp() {
        orderLineItem = OrderLineItem.of(1L, 1L, 1L, 1);
        orderTable = OrderTable.of(1L, 1L, 2, false);
        order = Order.of(1L, 1L, null, null, Arrays.asList(orderLineItem));
        order2 = Order.of(2L, 2L, null, null, Arrays.asList(orderLineItem));
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void create() {
        // given
        when(menuDao.countByIdIn(Arrays.asList(1L))).thenReturn(Long.valueOf(1));
        when(orderTableDao.findById(1L)).thenReturn(Optional.of(orderTable));
        when(orderDao.save(order)).thenReturn(order);
        when(orderLineItemDao.save(orderLineItem)).thenReturn(orderLineItem);

        //when
        Order expected = orderService.create(order);

        //then
        assertThat(order.getId()).isEqualTo(expected.getId());
    }

    @DisplayName("주문 항목이 없는 주문은 등록할 수 없다.")
    @Test
    void create2() {
        // given
        Order 주문_항목_없는_주문 = Order.of(1L, 1L, null, null, null);

        //then
        assertThatThrownBy(
                () -> orderService.create(주문_항목_없는_주문)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 없는 주문 항목이 있으면 주문을 등록할 수 없다.")
    @Test
    void create3() {
        // given
        when(menuDao.countByIdIn(Arrays.asList(1L))).thenReturn(Long.valueOf(0));

        //then
        assertThatThrownBy(
                () -> orderService.create(order)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블의 주문은 등록할 수 없다.")
    @Test
    void create4() {
        // given
        OrderTable 빈_테이블 = OrderTable.of(1L, 1L, 2, true);
        when(menuDao.countByIdIn(Arrays.asList(1L))).thenReturn(Long.valueOf(1));
        when(orderTableDao.findById(1L)).thenReturn(Optional.of(빈_테이블));

        //then
        assertThatThrownBy(
                () -> orderService.create(order)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() {
        // given
        List<Order> actual = Arrays.asList(order, order2);
        when(orderDao.findAll()).thenReturn(actual);
        when(orderLineItemDao.findAllByOrderId(any())).thenReturn(Arrays.asList(orderLineItem));

        //when
        List<Order> expected = orderService.list();

        //then
        assertThat(actual.size()).isEqualTo(expected.size());
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // given
        Order 조리중_주문 = Order.of(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), Arrays.asList(orderLineItem));
        Order 주문상태_변경_주문 = Order.of(조리중_주문.getId(), 조리중_주문.getOrderTableId(), OrderStatus.MEAL.name(), 조리중_주문.getOrderedTime(), 조리중_주문.getOrderLineItems());
        when(orderDao.findById(1L)).thenReturn(Optional.of(조리중_주문));
        when(orderDao.save(조리중_주문)).thenReturn(주문상태_변경_주문);

        //when
        Order expected = orderService.changeOrderStatus(order.getId(), 주문상태_변경_주문);

        //then
        assertThat(OrderStatus.MEAL.name()).isEqualTo(expected.getOrderStatus());
    }

    @DisplayName("계산 완료인 경우 주문 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatus2() {
        // given
        Order 계산완료_주문 = Order.of(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), Arrays.asList(orderLineItem));
        Order 주문상태_변경_주문 = Order.of(계산완료_주문.getId(), 계산완료_주문.getOrderTableId(), OrderStatus.MEAL.name(), 계산완료_주문.getOrderedTime(), 계산완료_주문.getOrderLineItems());
        when(orderDao.findById(1L)).thenReturn(Optional.of(계산완료_주문));

        //then
        assertThatThrownBy(
                () -> orderService.changeOrderStatus(order.getId(), 주문상태_변경_주문)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
