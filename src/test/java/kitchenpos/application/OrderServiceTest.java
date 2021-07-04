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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
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

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
    }

    @DisplayName("주어진 주문을 저장하고, 저장된 객체를 리턴한다.")
    @Test
    void create_order() {
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 2);
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, 1L, 1);
        OrderTable orderTable = new OrderTable(1L, 1L, 3, false);
        Order givenOrder = new Order(1L, 1L, OrderStatus.COOKING.name(), Arrays.asList(orderLineItem1, orderLineItem2));

        when(menuDao.countByIdIn(anyList()))
                .thenReturn(2L);
        when(orderTableDao.findById(any()))
                .thenReturn(Optional.of(orderTable));
        when(orderDao.save(any(Order.class)))
                .thenReturn(givenOrder);

        Order actual = orderService.create(givenOrder);

        assertThat(actual).isEqualTo(givenOrder);
    }

    @DisplayName("주문 저장시 비어있는 주문 항목 목록이 주어지면 예외를 던진다.")
    @Test
    void create_order_with_empty_order_lines() {
        final Order givenOrder = new Order(1L, 1L, OrderStatus.COOKING.name(), new ArrayList<>());
        assertThatThrownBy(() -> orderService.create(givenOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 저장시 주문항목 갯수와 메뉴의 갯수가 다르게 주어지면 예외를 던진다.")
    @Test
    void create_with_different_menu_size() {
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 2);
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, 1L, 1);
        Order givenOrder = new Order(1L, 1L, OrderStatus.COOKING.name(), Arrays.asList(orderLineItem1, orderLineItem2));
        when(menuDao.countByIdIn(anyList()))
                .thenReturn(1L);

        assertThatThrownBy(() -> orderService.create(givenOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 저장시 주문 테이블이 존재하지 않으면 예외를 던진다.")
    @Test
    void create_order_with_not_empty_order_table() {
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 2);
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, 1L, 1);
        Order givenOrder = new Order(1L, 1L, OrderStatus.COOKING.name(), Arrays.asList(orderLineItem1, orderLineItem2));
        when(menuDao.countByIdIn(anyList()))
                .thenReturn(2L);

        assertThatThrownBy(() -> orderService.create(givenOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문이 주문 테이블이 비어있는 상태로 주어지면 예외를 던진다.")
    @Test
    void create_order_with_empty_order_table() {
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 2);
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, 1L, 1);
        OrderTable orderTable = new OrderTable(1L, 1L, 3, true);
        Order givenOrder = new Order(1L, 1L, OrderStatus.COOKING.name(), Arrays.asList(orderLineItem1, orderLineItem2));

        when(menuDao.countByIdIn(anyList()))
                .thenReturn(2L);
        when(menuDao.countByIdIn(anyList()))
                .thenReturn(2L);
        when(orderTableDao.findById(any()))
                .thenReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> orderService.create(givenOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
