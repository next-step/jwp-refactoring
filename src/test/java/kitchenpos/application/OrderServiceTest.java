package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@DisplayName("주문 서비스 관련 테스트")
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
    private Order order;
    private OrderLineItem orderLineItem;
    private OrderLineItem orderLineItem2;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
        orderLineItem = new OrderLineItem();
        orderLineItem2 = new OrderLineItem();
        order = new Order(1L, 1L, "MEAL", LocalDateTime.now(), Lists.newArrayList(orderLineItem, orderLineItem2));
    }

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void createOrderTest() {
        when(menuDao.countByIdIn(any())).thenReturn(2L);
        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(new OrderTable(1L, 1L, 3, false)));
        when(orderDao.save(any())).thenReturn(order);
        when(orderLineItemDao.save(any())).thenReturn(orderLineItem).thenReturn(orderLineItem2);

        // when
        Order createdOrder = orderService.create(order);

        // then
        assertAll(
                () -> assertThat(createdOrder.getOrderLineItems().get(0)).isEqualTo(orderLineItem),
                () -> assertThat(createdOrder.getOrderLineItems().get(1)).isEqualTo(orderLineItem2)
        );
    }

    @DisplayName("메뉴에 존재하는 상품들은 모두 존재해야 한다.")
    @Test
    void createOrderExistProductExceptionTest() {
        assertThatThrownBy(() -> {
            // given
            final Order emptyOrderLineItem = new Order(1L, 1L, "MEAL", LocalDateTime.now(), Lists.emptyList());

            // when
            Order createdOrder = orderService.create(emptyOrderLineItem);

            // then
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목의 개수가 동일해야 한다.")
    @Test
    void createOrderSameSizeOrderLineItemExceptionTest() {
        assertThatThrownBy(() -> {
            when(menuDao.countByIdIn(any())).thenReturn(1L);
            // when
            Order createdOrder = orderService.create(order);

            // then
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 존재해야 한다.")
    @Test
    void createOrderExistOrderTableExceptionTest() {
        assertThatThrownBy(() -> {
            when(menuDao.countByIdIn(any())).thenReturn(2L);
            // given
            final Order emptyOrderTableItem = new Order(1L, null, "MEAL", LocalDateTime.now(), Lists.newArrayList(orderLineItem, orderLineItem2));

            // when
            Order createdOrder = orderService.create(emptyOrderTableItem);

            // then
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 존재해야 한다.")
    @Test
    void createOrderNotEmptyOrderTableExceptionTest() {
        assertThatThrownBy(() -> {
            when(menuDao.countByIdIn(any())).thenReturn(2L);
            when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(new OrderTable(1L, 1L, 3, true)));
            // given
            final Order emptyOrderTableItem = new Order(1L, 1L, "MEAL", LocalDateTime.now(), Lists.newArrayList(orderLineItem, orderLineItem2));

            // when
            Order createdOrder = orderService.create(emptyOrderTableItem);

            // then
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void getOrderTest() {
        when(orderDao.findAll()).thenReturn(Lists.newArrayList(order));

        // when
        List<Order> createdOrders = orderService.list();

        // then
        assertThat(createdOrders.get(0)).isEqualTo(order);
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatusTest() {
        when(orderDao.findById(anyLong())).thenReturn(Optional.of(order));

        // when
        Order changedOrders = orderService.changeOrderStatus(2L, order);

        // then
        assertThat(changedOrders).isEqualTo(order);
    }

    @DisplayName("주문 id는 반드시 존재한다.")
    @Test
    void changeOrderIdExceptionTest() {
        assertThatThrownBy(() -> {
            // when
            Order changedOrders = orderService.changeOrderStatus(null, order);

            // then
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태는 완료가 아니어야 한다.")
    @Test
    void changeOrderNotCompleteStatusExceptionTest() {
        assertThatThrownBy(() -> {
            when(orderDao.findById(anyLong())).thenReturn(Optional.of(order));
            // given
            order.setOrderStatus("COMPLETION");

            // when
            Order changedOrders = orderService.changeOrderStatus(2L, order);

            // then
        }).isInstanceOf(IllegalArgumentException.class);
    }


}