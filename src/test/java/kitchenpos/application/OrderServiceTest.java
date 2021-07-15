package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import org.graalvm.compiler.core.common.type.ArithmeticOpTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;
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


    @Test
    public void 주문생성_예외_주문테이블이주문할수없는테이블인경우() {
        //given
        Order order = mock(Order.class);
        given(order.getOrderLineItems()).willReturn(asList(new OrderLineItem()));

        given(menuDao.countByIdIn(anyList())).willReturn(1L);

        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));

        //when-then
        assertThatThrownBy(() ->orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void 주문생성_예외_주문테이블이없는경우() {
        //given
        Order order = mock(Order.class);
        given(order.getOrderLineItems()).willReturn(asList(new OrderLineItem()));

        given(menuDao.countByIdIn(anyList())).willReturn(1L);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.empty());

        //when-then
        assertThatThrownBy(() ->orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void 주문생성_예외_주문항목이메뉴에없는경우() {
        //given
        Order order = mock(Order.class);
        given(order.getOrderLineItems()).willReturn(asList(new OrderLineItem()));
        given(menuDao.countByIdIn(anyList())).willReturn(2L);
        //when-then
        assertThatThrownBy(() ->orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void 주문생성_예외_주문항목이없는경우() {
        //given
        Order order = mock(Order.class);
        given(order.getOrderLineItems()).willReturn(Collections.emptyList());

        //when-then
        assertThatThrownBy(() ->orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void 주문생성_성공() {
        // given
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(1L);
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(10L);

        Order order = new Order();
        order.setId(1L);
        order.setOrderLineItems(asList(orderLineItem));
        order.setOrderTableId(1L);

        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setEmpty(false);

        given(menuDao.countByIdIn(anyList())).willReturn(1L);
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));
        given(orderDao.save(any())).willReturn(order);
        given(orderLineItemDao.save(any())).willReturn(orderLineItem);

        //when
        Order savedOrder = orderService.create(order);

        //then
        assertThat(savedOrder.getId()).isEqualTo(1L);
    }

    @Test
    public void 주문조회_성공() {
        // given
        Order order = new Order();
        order.setId(1L);

        given(orderDao.findAll()).willReturn(asList(order));
        given(orderLineItemDao.findAllByOrderId(order.getId())).willReturn(asList(new OrderLineItem()));

        //when
        List<Order> savedOrder = orderService.list();

        //then
        assertThat(savedOrder.size()).isEqualTo(1L);
        assertThat(savedOrder.get(0).getId()).isEqualTo(1L);
    }

    @Test
    public void 주문상태변경_성공() {
        // given
        Order order = new Order();
        order.setOrderStatus(OrderStatus.MEAL.name());

        given(orderDao.findById(anyLong())).willReturn(Optional.of(order));

        given(orderDao.save(any())).willReturn(order);
        given(orderLineItemDao.findAllByOrderId(anyLong())).willReturn(asList(new OrderLineItem()));

        //when
        Order savedOrder = orderService.changeOrderStatus(1L, order);

        //then
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    public void 주문상태변경_예외_주문항목이없는경우() {
        //given
        Order order = mock(Order.class);
        given(orderDao.findById(anyLong())).willReturn(Optional.empty());

        //when-then
        assertThatThrownBy(() ->orderService.changeOrderStatus(1L, order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void 주문상태변경_예외_계산완료상태인경우() {
        //given
        Order order = new Order();
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        given(orderDao.findById(anyLong())).willReturn(Optional.of(order));

        //when-then
        assertThatThrownBy(() ->orderService.changeOrderStatus(1L, order)).isInstanceOf(IllegalArgumentException.class);
    }

}