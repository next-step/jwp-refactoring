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
        orders.add(order1);
        orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
    }

    @DisplayName("주문 등록 테스트")
    @Test
    void createOrder() {
        List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        when(menuDao.countByIdIn(menuIds)).thenReturn((long) orderLineItems.size());
        when(orderTableDao.findById(orderTable.getId())).thenReturn(java.util.Optional.ofNullable(orderTable));
        when(orderDao.save(order1)).thenReturn(order1);
        when(orderLineItemDao.save(orderLineItems.get(0))).thenReturn(orderLineItems.get(0));

        Order resultOrder = orderService.create(order1);

        assertAll(
                () -> assertThat(resultOrder.getOrderTableId()).isEqualTo(order1.getOrderTableId()),
                () -> assertThat(resultOrder.getOrderLineItems().size())
                        .isEqualTo(order1.getOrderLineItems().size()),
                () -> assertThat(resultOrder.getOrderLineItems().get(0).getMenuId())
                        .isEqualTo(order1.getOrderLineItems().get(0).getMenuId())
        );
    }

    @DisplayName("주문 등록 예외테스트: 주문 상품들이 없는경우")
    @Test
    void emptyOrderLineItems() {
        order1.setOrderLineItems(new ArrayList<>());
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.create(order1)
        );

        assertThat(exception.getMessage()).isEqualTo("주문상품이 없습니다.");
    }

    @DisplayName("주문 등록 예외테스트: 주문 상품들랑 메뉴갯수 일치하지 않는경우")
    @Test
    void notMatchOrderList() {
        when(menuDao.countByIdIn(any())).thenReturn(0L);

        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.create(order1)
        );

        assertThat(exception.getMessage()).isEqualTo("주문상품과 메뉴 갯수가 일치하지 않습니다.");
    }

    @DisplayName("주문 등록 예외테스트: 주문 테이블을 찾을 수 없는경우")
    @Test
    void notFoundOrderTable() {
        when(menuDao.countByIdIn(any())).thenReturn((long) orderLineItems.size());

        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.create(order1)
        );

        assertThat(exception.getMessage()).isEqualTo("주문 테이블을 찾을 수 없습니다.");
    }

    @DisplayName("주문 등록 예외테스트: 주문 테이블이 비어있는경우")
    @Test
    void emptyOrderTable() {
        when(menuDao.countByIdIn(any())).thenReturn((long) orderLineItems.size());
        orderTable.setEmpty(true);
        when(orderTableDao.findById(any())).thenReturn(java.util.Optional.ofNullable(orderTable));

        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.create(order1)
        );

        assertThat(exception.getMessage()).isEqualTo("테이블이 비어있지 않습니다.");
    }

    @DisplayName("주문목록 조회 테스트")
    @Test
    void findOrderList() {
        when(orderDao.findAll()).thenReturn(orders);
        when(orderLineItemDao.findAllByOrderId(any())).thenReturn(orderLineItems);

        List<Order> resultOrders = orderService.list();
        List<Long> orderTableIds =resultOrders.stream()
                .map(order -> order.getOrderTableId())
                .collect(Collectors.toList());
        List<Long> expectedOrderTableIds =orders.stream()
                .map(order -> order.getOrderTableId())
                .collect(Collectors.toList());

        assertThat(resultOrders.size()).isEqualTo(orders.size());
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

    @DisplayName("주문 상태변경 예외테스트: 주문을 찾을 수 없는 경우")
    @Test
    void notFoundOrder() {
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.changeOrderStatus(1L, order1)
        );

        assertThat(exception.getMessage()).isEqualTo("주문을 찾을 수 없습니다.");
    }
    @DisplayName("주문 상태변경 예외테스트: 주문상태가 올바르지 않은 경우")
    @Test
    void invalidOrderStatus() {
        when(orderDao.findById(any())).thenReturn(java.util.Optional.ofNullable(order1));
        order1.setOrderStatus(OrderStatus.COMPLETION.name());

        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.changeOrderStatus(1L, order1)
        );

        assertThat(exception.getMessage()).isEqualTo("주문이 완료된 상태입니다.");


    }
}
