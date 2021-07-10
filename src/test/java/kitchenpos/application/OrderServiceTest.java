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
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;

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
        final OrderLineItemRequest orderLineItemRequest1 = new OrderLineItemRequest(1L, 1);
        final OrderLineItemRequest orderLineItemRequest2 = new OrderLineItemRequest(2L, 1);
        final List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(orderLineItemRequest1, orderLineItemRequest2);
        OrderRequest orderRequest = new OrderRequest(1L, orderLineItemRequests);
        order.setOrderLineItems(Arrays.asList(orderLineItem, orderLineItem2));
        final Menu menu1 = mock(Menu.class);
        final Menu menu2 = mock(Menu.class);
        given(menu1.getId()).willReturn(1L);
        given(menu2.getId()).willReturn(2L);
        given(menuDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(menu1, menu2));
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(new OrderTable()));

        savedOrder.setId(1L);
        given(orderDao.save(any(Order.class))).willReturn(savedOrder);

        // when
        orderService.create(orderRequest);

        // then
        verify(orderDao).save(any(Order.class));
    }

    @DisplayName("아이템이 없는 주문을 생성할 때 예외가 발생하는지 테스트")
    @Test
    void given_OrderHasEmptyItem_when_Create_then_ThrownException() {
        // given
        OrderRequest order = new OrderRequest();

        // when
        final Throwable throwable = catchThrowable(() -> orderService.create(order));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("아이템이 하나만 있는 주문을 생성할 때 예외가 발생하는지 테스트")
    @Test
    void given_OrderHasOnlyOneItem_when_Create_then_ThrownException() {
        // given
        OrderRequest order = new OrderRequest(1L, Collections.singletonList(new OrderLineItemRequest()));

        // when
        final Throwable differentSizeException = catchThrowable(() -> orderService.create(order));

        // then
        assertThat(differentSizeException).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        // given
        given(orderDao.findAll()).willReturn(Collections.singletonList(order));

        // when
        orderService.list();

        // then
        verify(orderDao).findAll();
    }

    @Test
    void changeOrderStatus() {
        // given
        OrderRequest order = new OrderRequest();
        order.setOrderStatus("COOKING");
        Order savedOrder = new Order();
        given(orderDao.findById(orderId)).willReturn(Optional.of(savedOrder));

        // when
        orderService.changeOrderStatus(orderId, order);

        // then
        verify(orderDao).save(savedOrder);
        assertThat(savedOrder.getOrderStatus()).isEqualTo(order.getOrderStatus());
    }

    @Test
    void given_CompletedOrder_when_ChangeOrderStatus_then_ThrownException() {
        // given
        OrderRequest order = new OrderRequest();
        order.setOrderStatus("COMPLETION");

        // when
        final Throwable throwable = catchThrowable(() -> orderService.changeOrderStatus(orderId, order));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }
}
