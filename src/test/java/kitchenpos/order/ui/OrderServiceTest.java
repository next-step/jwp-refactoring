package kitchenpos.order.ui;

import kitchenpos.menu.dao.MenuDao;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.exceptions.InputOrderDataErrorCode;
import kitchenpos.order.exceptions.InputOrderDateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("주문 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("주문을 등록한다.")
    void createOrderTest() {

        Order order = mock(Order.class);

        when(order.getId())
                .thenReturn(1L);

        OrderLineItem orderLineItem = mock(OrderLineItem.class);
        when(order.getOrderLineItems())
                .thenReturn(Arrays.asList(orderLineItem));

        OrderTable orderTable = mock(OrderTable.class);
        when(orderTableDao.findById(anyLong()))
                .thenReturn(Optional.of(orderTable));

        when(menuDao.countByIdIn(anyList()))
                .thenReturn(1L);

        Order savedOrder = mock(Order.class);

        when(orderDao.save(order)).thenReturn(savedOrder);

        OrderLineItem savedOrderLineItem = mock(OrderLineItem.class);
        when(orderLineItemDao.save(orderLineItem))
                .thenReturn(savedOrderLineItem);

        when(savedOrder.getOrderLineItems())
                .thenReturn(Arrays.asList(savedOrderLineItem));

        when(savedOrder.getId())
                .thenReturn(1L);
        orderService.create(order);

        //then
        assertThat(savedOrder.getId()).isEqualTo(order.getId());
        assertThat(savedOrder.getOrderLineItems()).contains(savedOrderLineItem);
    }

    @Test
    @DisplayName("주문상태를 변경한다. 요리중 -> 식사중")
    void modifyOrderStatusTest() {
        //given
        Order order = mock(Order.class);

        //when
        order.startCooking();

        when(order.getId())
                .thenReturn(1L);

        when(orderDao.findById(anyLong()))
                .thenReturn(Optional.of(order));

        when(order.getOrderStatus())
                .thenReturn(OrderStatus.MEAL.name());

        orderService.changeOrderStatus(order.getId(), order);

        //then
        assertThat(order.getId()).isEqualTo(1L);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    @DisplayName("주문 내역을 조회한다.")
    void findOrderTest() {
        //given
        Order order = mock(Order.class);

        //when
        when(order.getOrderStatus())
                .thenReturn(OrderStatus.COOKING.name());

        when(order.getId())
                .thenReturn(1L);

        when(order.getOrderStatus())
                .thenReturn(OrderStatus.MEAL.name());

        when(orderDao.findAll())
                .thenReturn(Arrays.asList(order));
        //then
        List<Order> orders = orderService.list();

        assertThat(orders).contains(order);

    }

    @Test
    @DisplayName("주문상태를 변경한다. 완료 후에 다시 상태를 변경 할 수 없다.  완료 -> 요리중")
    void modifyOrderStatusErrorTest() {
        //given
        Order order = mock(Order.class);

        when(order.getId())
                .thenReturn(1L);

        when(orderDao.findById(anyLong()))
                .thenReturn(Optional.of(order));
        //when
        when(order.getOrderStatus())
                .thenReturn(OrderStatus.COMPLETION.name());

        assertThatThrownBy(() -> {
            order.startMeal();
            orderService.changeOrderStatus(order.getId(), order);
        }).isInstanceOf(InputOrderDateException.class)
                .hasMessageContaining(InputOrderDataErrorCode.THE_ORDER_STATUS_DO_NOT_CHANGE_COMPLETION_TO_ANY_OTHER.errorMessage());
    }
}
