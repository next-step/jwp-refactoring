package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
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

    @InjectMocks
    private OrderService orderService;

    private final Long orderId = 1L;
    private Order order;
    private Order savedOrder;
    private OrderLineItem orderLineItem;
    private OrderLineItem orderLineItem2;

    @BeforeEach
    void setUp() {
        order = new Order();
        savedOrder = new Order();
        orderLineItem = new OrderLineItem();
        orderLineItem2 = new OrderLineItem();
    }

    @Test
    void create() {
        // given
        order.setOrderTableId(1L);
        order.setOrderLineItems(Arrays.asList(orderLineItem, orderLineItem2));
        when(menuDao.countByIdIn(anyList())).thenReturn(2L);
        when(orderTableDao.findById(order.getOrderTableId())).thenReturn(Optional.of(new OrderTable()));

        savedOrder.setId(1L);
        when(orderDao.save(order)).thenReturn(savedOrder);

        OrderLineItem savedOrderLineItem = new OrderLineItem();
        OrderLineItem savedOrderLineItem2 = new OrderLineItem();
        when(orderLineItemDao.save(orderLineItem)).thenReturn(savedOrderLineItem);
        when(orderLineItemDao.save(orderLineItem2)).thenReturn(savedOrderLineItem2);

        // when
        final Order actual = orderService.create(order);

        // then
        verify(orderDao).save(order);
        verify(orderLineItemDao).save(orderLineItem);
        verify(orderLineItemDao).save(orderLineItem2);
        assertThat(actual.getOrderLineItems()).isEqualTo(Arrays.asList(savedOrderLineItem, savedOrderLineItem2));
    }

    @DisplayName("아이템이 없는 주문을 생성할 때 예외가 발생하는지 테스트")
    @Test
    void given_OrderHasEmptyItem_when_Create_then_ThrownException() {
        // given
        Order order = new Order();

        // when
        final Throwable throwable = catchThrowable(() -> orderService.create(order));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("아이템이 하나만 있는 주문을 생성할 때 예외가 발생하는지 테스트")
    @Test
    void given_OrderHasOnlyOneItem_when_Create_then_ThrownException() {
        // given
        Order order = new Order();
        order.setOrderLineItems(Collections.singletonList(new OrderLineItem()));
        given(menuDao.countByIdIn(anyList())).willReturn(2L);

        // when
        final Throwable differentSizeException = catchThrowable(() -> orderService.create(order));

        // then
        assertThat(differentSizeException).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        // given
        order.setId(1L);
        when(orderDao.findAll()).thenReturn(Collections.singletonList(order));
        final List<OrderLineItem> orderLineItems = Collections.singletonList(orderLineItem);
        when(orderLineItemDao.findAllByOrderId(order.getId())).thenReturn(orderLineItems);

        // when
        final List<Order> actual = orderService.list();

        // then
        verify(orderDao).findAll();
        assertThat(actual.get(0).getOrderLineItems()).isEqualTo(orderLineItems);
    }

    @Test
    void changeOrderStatus() {
        // given
        order.setOrderStatus("COOKING");
        Order savedOrder = new Order();
        when(orderDao.findById(orderId)).thenReturn(Optional.of(savedOrder));

        // when
        orderService.changeOrderStatus(orderId, order);

        // then
        verify(orderDao).save(savedOrder);
        assertThat(savedOrder.getOrderStatus()).isEqualTo(order.getOrderStatus());
    }

    @Test
    void given_CompletedOrder_when_ChangeOrderStatus_then_ThrownException() {
        // given
        order.setOrderStatus("COMPLETION");

        // when
        final Throwable throwable = catchThrowable(() -> orderService.changeOrderStatus(orderId, order));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }
}
