package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.application.MenuServiceTest.generateMenu;
import static kitchenpos.application.TableServiceTest.generateOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("주문")
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    private Menu menu1;
    private Menu menu2;

    private OrderTable orderTable;

    private OrderLineItem orderLineItem1;
    private OrderLineItem orderLineItem2;

    private Order order;

    @BeforeEach
    void setUp() {
        menu1 = generateMenu(1L, "menu1", null, 1L, null);
        menu2 = generateMenu(1L, "menu2", null, 1L, null);

        orderTable = generateOrderTable(1L, 0, false);

        orderLineItem1 = generateOrderLineItem(menu1.getId(), 2L);
        orderLineItem2 = generateOrderLineItem(menu2.getId(), 1L);

        order = generateOrder(1L, orderTable.getId(), Arrays.asList(orderLineItem1, orderLineItem2));
    }

    @Test
    @DisplayName("전체 주문을 조회할 수 있다.")
    void orderTest1() {
        given(orderDao.findAll()).willReturn(Arrays.asList(order));
        given(orderLineItemDao.findAllByOrderId(order.getId())).willReturn(order.getOrderLineItems());

        List<Order> orders = orderService.list();
        assertThat(orders.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("새로운 주문을 추가할 수 있다.")
    void orderTest2() {
        given(menuDao.countByIdIn(Arrays.asList(menu1.getId(), menu2.getId()))).willReturn(2L);
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(orderTable));
        given(orderDao.save(order)).willReturn(order);
        given(orderLineItemDao.save(orderLineItem1)).willReturn(orderLineItem1);
        given(orderLineItemDao.save(orderLineItem2)).willReturn(orderLineItem2);

        Order createdOrder = orderService.create(order);
        assertThat(createdOrder.getId()).isEqualTo(order.getId());
    }

    @Test
    @DisplayName("새로운 주문 추가 : 주문항목은 존재하는 메뉴들로만 구성되야 한다.")
    void orderTest3() {
        Order nullOrderItem = generateOrder(1L, orderTable.getId(), null);

        assertThatThrownBy(() -> orderService.create(nullOrderItem)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("새로운 주문 추가 : 존재하지 않는 주문 테이블로 요청할 수 없다.")
    void orderTest4() {
        given(menuDao.countByIdIn(Arrays.asList(menu1.getId(), menu2.getId()))).willReturn(1L);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 현황을 수정할 수 있다.")
    void orderTest5() {
        given(orderDao.findById(order.getId())).willReturn(Optional.of(order));
        given(orderDao.save(order)).willReturn(order);
        given(orderLineItemDao.findAllByOrderId(order.getId())).willReturn(Arrays.asList(orderLineItem1, orderLineItem2));

        Order changeOrder = generateOrder(OrderStatus.COMPLETION.name());
        Order changedOrder = orderService.changeOrderStatus(order.getId(), changeOrder);
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @Test
    @DisplayName("주문 현황 수정 : 존재하지 않는 주문으로 요청할 수 없다.")
    void orderTest6() {
        Order changeOrder = generateOrder(OrderStatus.COMPLETION.name());
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), changeOrder)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 현황 수정 : 완료된 상태의 주문 현황은 수정할 수 없다.")
    void orderTest7() {
        Order changeOrder = generateOrder(OrderStatus.COMPLETION.name());
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        given(orderDao.findById(order.getId())).willReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), changeOrder)).isInstanceOf(IllegalArgumentException.class);

    }

    public static OrderLineItem generateOrderLineItem(Long menuId, long quantity) {
        return OrderLineItem.of(null, null, menuId, quantity);
    }

    public static Order generateOrder(Long id, Long orderTableId, List<OrderLineItem> orderLineItems) {
        return Order.of(id, orderTableId, null, null, orderLineItems);
    }

    public static Order generateOrder(String orderStatus) {
        return Order.of(null, null, orderStatus, null, null);
    }

}