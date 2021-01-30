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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @InjectMocks
    private OrderService orderService;

    private Order order;

    private OrderLineItem orderLineItem;

    @BeforeEach
    void setUp() {
        orderLineItem = new OrderLineItem(1L,1L, 1);
        order = new Order(1L, Collections.singletonList(orderLineItem));
    }

    @DisplayName("주문 생성")
    @Test
    void createOrder() {
        // given
        Long menuId = orderLineItem.getMenuId();
        when(menuDao.countByIdIn(Collections.singletonList(menuId))).thenReturn(1L);

        OrderTable orderTable = new OrderTable();
        orderTable.setId(order.getOrderTableId());
        when(orderTableDao.findById(order.getOrderTableId())).thenReturn(Optional.of(orderTable));
        when(orderDao.save(order)).thenReturn(order);
        when(orderLineItemDao.save(orderLineItem)).thenReturn(orderLineItem);

        // when
        Order actual = orderService.create(this.order);

        // then
        assertThat(actual.getId()).isEqualTo(order.getId());
        assertThat(actual.getOrderTableId()).isEqualTo(order.getOrderTableId());
        assertThat(actual.getOrderLineItems()).isEqualTo(order.getOrderLineItems());
        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(actual.getOrderedTime()).isNotNull();
    }

    @DisplayName("하나 이상의 주문 항목이 있어야 함")
    @Test
    void orderLineItem() {
        // given
        Order order = new Order(1L, null);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.create(order);
        });
    }

    @DisplayName("주문 테이블 상태가 비어있는 경우")
    @Test
    void createNotEmpty() {
        // given
        Long menuId = orderLineItem.getMenuId();
        when(menuDao.countByIdIn(Collections.singletonList(menuId))).thenReturn(1L);

        OrderTable orderTable = new OrderTable();
        orderTable.setId(order.getOrderTableId());
        orderTable.setEmpty(true);
        when(orderTableDao.findById(order.getOrderTableId())).thenReturn(Optional.of(orderTable));

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.create(order);
        });
    }

    @DisplayName("주문 목록을 조회")
    @Test
    void selectOrders() {
        // given
        when(orderDao.findAll()).thenReturn(Collections.singletonList(order));

        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders.get(0).getId()).isEqualTo(order.getId());
        assertThat(orders.get(0).getOrderTableId()).isEqualTo(order.getOrderTableId());
        assertThat(orders.get(0).getOrderLineItems()).isEqualTo(order.getOrderLineItems());
        assertThat(orders.get(0).getOrderStatus()).isEqualTo(order.getOrderStatus());
        assertThat(orders.get(0).getOrderedTime()).isEqualTo(order.getOrderedTime());
    }

    @DisplayName("주문의 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        // given
        order.setOrderStatus(OrderStatus.MEAL.name());
        when(orderDao.findById(order.getId())).thenReturn(Optional.of(order));

        Order order = new Order();
        order.setOrderStatus(OrderStatus.COMPLETION.name());

        // when
        Order updatedOrder = orderService.changeOrderStatus(this.order.getId(), order);

        // then
        assertThat(updatedOrder.getOrderStatus()).isEqualTo(order.getOrderStatus());
    }

    @DisplayName("주문 상태가 완료인 경우 변경할 수 없다.")
    @Test
    void notChangeStatusIsComplete() {
        // given
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        when(orderDao.findById(order.getId())).thenReturn(Optional.of(order));

        Order order = new Order();
        order.setOrderStatus(OrderStatus.MEAL.name());

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.changeOrderStatus(this.order.getId(), order);
        });
    }
}
