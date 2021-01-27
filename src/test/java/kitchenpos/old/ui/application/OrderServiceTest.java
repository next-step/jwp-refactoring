package kitchenpos.old.ui.application;

import kitchenpos.old.ui.dao.MenuDao;
import kitchenpos.old.ui.dao.OrderDao;
import kitchenpos.old.ui.dao.OrderLineItemDao;
import kitchenpos.old.ui.dao.OrderTableDao;
import kitchenpos.old.ui.domain.Order;
import kitchenpos.old.ui.domain.OrderLineItem;
import kitchenpos.old.ui.domain.OrderStatus;
import kitchenpos.old.ui.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

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

    @DisplayName("주문하기")
    @Test
    public void create() {
        Order order = new Order();
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1l);
        orderLineItems.add(orderLineItem);

        order.setOrderLineItems(orderLineItems);

        given(menuDao.countByIdIn(any()))
                .willReturn(1l);

        OrderTable mockOrderTable = mock(OrderTable.class);
        given(orderTableDao.findById(any()))
                .willReturn(Optional.of(mockOrderTable));
        given(mockOrderTable.isEmpty())
                .willReturn(false);
        given(orderLineItemDao.save(any()))
                .willReturn(orderLineItem);

        Order mockSavedOrder = mock(Order.class);
        given(mockSavedOrder.getId())
                .willReturn(99l);
        given(orderDao.save(any()))
                .willReturn(mockSavedOrder);

        orderService.create(order);
        assertThat(orderLineItem.getOrderId())
                .isEqualTo(99l);
        verify(mockSavedOrder, times(1))
                .setOrderLineItems(orderLineItems);
    }

    @DisplayName("주문 등록 불가능한 케이스 1 - 주문내용이 없는 경우")
    @Test
    public void invalidCreateCase1() {
        assertThatThrownBy(() -> {
            orderService.create(new Order());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 불가능한 케이스 2 - 중복된 메뉴의 주문이 있는 경우")
    @Test
    public void invalidCreateCase2() {
        Order order = new Order();
        List<OrderLineItem> orderLineItems = new ArrayList<>();

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1l);
        orderLineItems.add(orderLineItem);

        OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem2.setMenuId(1l);
        orderLineItems.add(orderLineItem2);

        order.setOrderLineItems(orderLineItems);

        given(menuDao.countByIdIn(any()))
                .willReturn(1l);
        assertThatThrownBy(() -> {
            orderService.create(order);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 불가능한 케이스 3 - 주문한 테이블이 유효하지 않은 경우")
    @Test
    public void invalidCreateCase3() {
        Order order = new Order();
        List<OrderLineItem> orderLineItems = new ArrayList<>();

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1l);
        orderLineItems.add(orderLineItem);

        order.setOrderLineItems(orderLineItems);

        given(menuDao.countByIdIn(any()))
                .willReturn(1l);
        given(orderTableDao.findById(any()))
                .willReturn(Optional.empty());
        assertThatThrownBy(() -> {
            orderService.create(order);
        }).isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("주문 등록 불가능한 케이스 4 - 비어있는 테이블에 대한 주문인 경우")
    @Test
    public void invalidCreateCase4() {
        Order order = new Order();
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1l);
        orderLineItems.add(orderLineItem);

        order.setOrderLineItems(orderLineItems);

        given(menuDao.countByIdIn(any()))
                .willReturn(1l);

        OrderTable mockOrderTable = mock(OrderTable.class);
        given(orderTableDao.findById(any()))
                .willReturn(Optional.of(mockOrderTable));
        given(mockOrderTable.isEmpty())
                .willReturn(true);

        assertThatThrownBy(() -> {
            orderService.create(order);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태 변경")
    @Test
    public void changeStatus() {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.COMPLETION.name());

        Order mockOrder = mock(Order.class);
        given(mockOrder.getOrderStatus())
                .willReturn(OrderStatus.MEAL.name());
        given(orderDao.findById(any()))
                .willReturn(Optional.of(mockOrder));
        given(orderLineItemDao.findAllByOrderId(anyLong()))
                .willReturn(Collections.emptyList());

        orderService.changeOrderStatus(1l, order);

        verify(mockOrder, times(1))
                .setOrderStatus(OrderStatus.COMPLETION.name());
        verify(mockOrder, times(1))
                .setOrderLineItems(Collections.emptyList());
    }

    @DisplayName("주문 상태 변경 불가능한 케이스 1 - 주문이 유효하지 않은 경우")
    @Test
    public void invalidChangeStatusCase1() {
        given(orderDao.findById(any()))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(1l, new Order());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태 변경 불가능한 케이스 2 - 이미 계산이 완료된 경우")
    @Test
    public void invalidChangeStatusCase2() {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.COMPLETION.name());

        Order mockOrder = mock(Order.class);
        given(mockOrder.getOrderStatus())
                .willReturn(OrderStatus.COMPLETION.name());
        given(orderDao.findById(any()))
                .willReturn(Optional.of(mockOrder));

        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(1l, order);
        }).isInstanceOf(IllegalArgumentException.class);
    }

}
