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
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private Order order1;
    private Order order2;
    private Order order3;
    private OrderTable orderTable;
    private List<Order> orders = new ArrayList<>();
    private List<OrderLineItem> orderLineItems = new ArrayList<>();
    private OrderService orderService;

    @Mock
    private MenuDao menuDao;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderLineItemDao orderLineItemDao;
    @Mock
    private OrderTableDao orderTableDao;

    OrderServiceTest() {
    }

    @BeforeEach
    public void setUp() {
        orderLineItems.add(new OrderLineItem(1L, 5));
        orderLineItems.add(new OrderLineItem(2L, 2));
        orderTable = new OrderTable(11L);
        order1 = new Order(11L, orderLineItems);
        order2 = new Order(11L, orderLineItems);
        order3 = new Order(11L, orderLineItems);
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);
        orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
    }

    @DisplayName("주문 등록 테스트")
    @Test
    void createOrder() {
        when(menuDao.countByIdIn(any())).thenReturn((long) orderLineItems.size());
        when(orderTableDao.findById(any())).thenReturn(java.util.Optional.ofNullable(orderTable));
        mockSaveOrder(order1);
        mockSaveOrder(order2);
        mockSaveOrder(order3);
        mockSaveOrderLineItem(orderLineItems.get(0));
        mockSaveOrderLineItem(orderLineItems.get(1));

        checkOrder(orderService.create(order1), order1);
        checkOrder(orderService.create(order2), order2);
        checkOrder(orderService.create(order3), order3);
    }
    @DisplayName("주문목록 조회 테스트")
    @Test
    void findOrderList() {
        when(orderDao.findAll()).thenReturn(orders);
        when(orderLineItemDao.findAllByOrderId(any())).thenReturn(orderLineItems);

        List<Order> resultOrders = orderService.list();

        assertThat(resultOrders.size()).isEqualTo(orders.size());
        List<Long> orderTableIds =resultOrders.stream()
                .map(order -> order.getOrderTableId())
                .collect(Collectors.toList());
        List<Long> expectedOrderTableIds =orders.stream()
                .map(order -> order.getOrderTableId())
                .collect(Collectors.toList());

        assertThat(orderTableIds).containsExactlyElementsOf(expectedOrderTableIds);
    }

    @DisplayName("주문 상태변화 테스트")
    @Test
    void changeOrderStatus() {
        when(orderDao.findById(any())).thenReturn(java.util.Optional.ofNullable(order1));
        when(orderDao.save(order1)).thenReturn(order1);
        when(orderLineItemDao.findAllByOrderId(any())).thenReturn(orderLineItems);

        order1.setOrderStatus(OrderStatus.MEAL.toString());
        Order resultOrder = orderService.changeOrderStatus(1L, order1);

        assertThat(resultOrder.getOrderStatus()).isEqualTo(order1.getOrderStatus());
    }

    private void checkOrder(Order resultOrder, Order order) {
        assertThat(resultOrder.getOrderTableId()).isEqualTo(order.getOrderTableId());
        assertThat(resultOrder.getOrderLineItems().size())
                .isEqualTo(order.getOrderLineItems().size());
        assertThat(resultOrder.getOrderLineItems().get(0).getMenuId())
                .isEqualTo(order.getOrderLineItems().get(0).getMenuId());
    }

    private void mockSaveOrderLineItem(OrderLineItem orderLineItem) {
        when(orderLineItemDao.save(orderLineItem)).thenReturn(orderLineItem);
    }

    private void mockSaveOrder(Order order) {
        when(orderDao.save(order)).thenReturn(order);
    }

}