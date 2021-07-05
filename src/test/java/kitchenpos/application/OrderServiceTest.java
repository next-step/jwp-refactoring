package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import kitchenpos.dao.OrderDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.repository.MenuDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.order.domain.Order;
import kitchenpos.domain.OrderTable;

@DisplayName("주문 테스트")
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

    private Order order;
    private OrderRequest orderRequest;

    @BeforeEach
    void setup() {
        order = new Order();
        order.setId(1L);
        order.setOrderStatus(COOKING.name());

        Long orderTableId = 1L;
        Long menuId = 1L;
        Long quantity = 1L;
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menuId, quantity);

        orderRequest = new OrderRequest(orderTableId, Arrays.asList(orderLineItemRequest));
    }

    @DisplayName("사용자는 주문을 생성 할 수 있다.")
    @Test
    void create() {
        // given
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        order.setOrderLineItems(Arrays.asList(orderLineItem));

        // when
        when(orderDao.save(any())).thenReturn(order);
        when(menuDao.findAllById(any())).thenReturn(Arrays.asList(new Menu()));
        when(orderTableDao.findById(any())).thenReturn(Optional.of(new OrderTable()));
        Order createdOrder = orderService.create(orderRequest);

        // then
        assertThat(createdOrder.getOrderStatus()).isEqualTo(COOKING.name());
    }

    @DisplayName("사용자는 주문 리스트를 조회 할 수 있다.")
    @Test
    void findAll() {
        // given

        // when
        when(orderDao.findAll()).thenReturn(Arrays.asList(order));
        when(orderLineItemDao.findAllByOrderId(order.getId())).thenReturn(Arrays.asList(new OrderLineItem()));
        List<Order> orders = orderService.list();
        // then
        assertThat(orders.size()).isEqualTo(1);
        assertThat(orders.get(0).getId()).isEqualTo(1L);
        assertThat(order.getOrderLineItems().size()).isEqualTo(1L);
    }

    @DisplayName("주문 생성 시 주문 상태를 요리중으로 한다.")
    @Test
    void changeOrderStatus() {
        // given
        Order targetOrder = new Order();
        targetOrder.setOrderStatus(MEAL.name());
        order.setOrderStatus(COOKING.name());

        // when
        when(orderDao.findById(1L)).thenReturn(Optional.of(order));
        Order changedOrder = orderService.changeOrderStatus(1L, targetOrder);
        // then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(MEAL.name());
    }

    @DisplayName("사용자는 주문시 주문테이블id, 그리고 메뉴id와 수량을 요청으로 한다.")
    @Test
    void createFailedByOrderLineItems() {
        // given
        // when
        orderRequest = new OrderRequest();
        // then
        assertThatThrownBy(() -> orderService.create(orderRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("사용자는 주문시 주문테이블id, 그리고 메뉴id와 수량을 요청으로 한다.")
    @Test
    void createFailedByMenus() {
        // given
        List<OrderLineItemRequest> orderLineItems = new ArrayList<>();
        orderLineItems.add(new OrderLineItemRequest());
        orderLineItems.add(new OrderLineItemRequest());

        orderRequest = new OrderRequest(1L, orderLineItems);

        // when
        // then
        assertThatThrownBy(() -> orderService.create(orderRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("요청시 기입한 주문테이블이 존재해야한다.")
    @Test
    void createFailedByOrderTable() {
        // given
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        order.setOrderLineItems(Arrays.asList(orderLineItem));
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);

        // when
        // then
        assertThatThrownBy(() -> orderService.create(orderRequest)).isInstanceOf(IllegalArgumentException.class);
    }
}