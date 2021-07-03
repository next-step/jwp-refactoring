package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

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
    OrderService orderService;
    private Order order;
    private List<OrderLineItem> orderLineItems;
    private OrderLineItem orderLineItem1;
    private OrderLineItem orderLineItem2;
    private OrderTable orderTable;
    private final static long ANY_ORDER_ID = 1L;
    private final static long ANY_ORDER_TABLE_ID = 1L;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(ANY_ORDER_ID);
        orderLineItems = new ArrayList<>();

        orderLineItem1 = new OrderLineItem();

        orderLineItem1.setOrder(order);
        orderLineItem1.setMenuId(1L);

        orderLineItem2 = new OrderLineItem();
        orderLineItem2.setOrder(order);
        orderLineItem2.setMenuId(2L);

        orderTable = new OrderTable();
        orderTable.setId(ANY_ORDER_ID);
        orderTable.setEmpty(true);
    }

    @Test
    @DisplayName("주문 항목이 비어있다면 주문을 등록할 수 없다.")
    void exception_create() {

        order.setOrderLineItems(orderLineItems);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("should have orderLineItems");
    }

    @Test
    @DisplayName("주문 항목의 갯수가 주문 항목의 메뉴의 갯수와 일치 하지 않으면 등록할 수 없다.")
    void exception2_create() {
        orderLineItems.add(orderLineItem1);
        orderLineItems.add(orderLineItem2);
        order.setOrderLineItems(orderLineItems);

        given(menuDao.countByIdIn(Lists.list(1L, 2L))).willReturn(100L);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Not Same as orderLineItems");
    }

    @Test
    @DisplayName("주문의 주문 테이블이 빈 테이블일 경우 주문을 등록할 수 없다.")
    void exception3_create() {
        orderLineItems.add(orderLineItem1);
        orderLineItems.add(orderLineItem2);
        order.setOrderLineItems(orderLineItems);

        orderTable.setEmpty(true);
        given(menuDao.countByIdIn(Lists.list(1L, 2L))).willReturn(2L);
        order.setOrderTable(orderTable);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Should have not orderTable empty");
    }

    @Test
    @DisplayName("처음 주문 상태(order status)는 조리(COOKING) 상태가 된다.")
    void after_create_orderStatus_is_COOKING() {
        orderLineItems.add(orderLineItem1);
        orderLineItems.add(orderLineItem2);
        order.setOrderLineItems(orderLineItems);

        given(menuDao.countByIdIn(Lists.list(1L, 2L))).willReturn(2L);
        orderTable.setEmpty(false);

        order.setOrderTable(orderTable);

        given(orderDao.save(order)).willReturn(order);

        Order order = orderService.create(this.order);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @Test
    @DisplayName("주문의 주문 상태(order status)를 식사 상태로 변경할 수 있다.")
    void changeOrderStatusTest() {
        given(orderDao.findById(ANY_ORDER_ID)).willReturn(Optional.of(order));
        given(orderDao.save(order)).willReturn(order);
        given(orderLineItemDao.findAllByOrderId(ANY_ORDER_ID)).willReturn(new ArrayList<>());

        order.setOrderStatus(OrderStatus.MEAL);
        Order changedOrder = orderService.changeOrderStatus(ANY_ORDER_ID, this.order);
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
        verify(orderDao).save(order);
    }

    @Test
    @DisplayName("이미 계산이 완료된 주문은 주문 상태(order status)를 바꿀 수 없다.")
    void exception_changeOrderStatusTest() {
        given(orderDao.findById(ANY_ORDER_ID)).willReturn(Optional.of(order));

        order.setOrderStatus(OrderStatus.COMPLETION);
        assertThatThrownBy(() -> orderService.changeOrderStatus(ANY_ORDER_ID, order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid OrderStatus");

    }
}