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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(value = MockitoExtension.class)
@DisplayName("주문에 대한 비즈니스 로직")
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
        orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(1L);
        orderLineItem.setOrderId(1L);
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1);

        order = new Order();
        order.setId(1L);
        order.setOrderTableId(1L);
        order.setOrderLineItems(Arrays.asList(orderLineItem));
    }

    @DisplayName("주문을 생성할 수 있다.")
    @Test
    void create() {
        // given
        Long menuId = orderLineItem.getMenuId();
        when(menuDao.countByIdIn(Arrays.asList(menuId))).thenReturn(1L);
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

    @DisplayName("하나 이상의 주문 항목을 가져야 한다.")
    @Test
    void requiredLineItem() {
        // given
        Order noneItemOrder = new Order();
        order.setId(1L);
        order.setOrderTableId(1L);

        // when / then
        assertThrows(IllegalArgumentException.class, () -> orderService.create(noneItemOrder));
    }

    @DisplayName("주문 테이블이 비어있는 경우 생성할 수 없다.")
    @Test
    void emptyTable() {
        // given
        Long menuId = orderLineItem.getMenuId();
        when(menuDao.countByIdIn(Arrays.asList(menuId))).thenReturn(1L);
        OrderTable orderTable = new OrderTable();
        orderTable.setId(order.getOrderTableId());
        orderTable.setEmpty(true);
        when(orderTableDao.findById(order.getOrderTableId())).thenReturn(Optional.of(orderTable));

        // when / then
        assertThrows(IllegalArgumentException.class, () -> orderService.create(order));
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void findAll() {
        // given
        when(orderDao.findAll()).thenReturn(Arrays.asList(order));

        // when
        List<Order> list = orderService.list();

        // then
        assertThat(list.get(0).getId()).isEqualTo(order.getId());
        assertThat(list.get(0).getOrderTableId()).isEqualTo(order.getOrderTableId());
        assertThat(list.get(0).getOrderLineItems()).isEqualTo(order.getOrderLineItems());
        assertThat(list.get(0).getOrderStatus()).isEqualTo(order.getOrderStatus());
        assertThat(list.get(0).getOrderedTime()).isEqualTo(order.getOrderedTime());
    }

    @DisplayName("주문의 상태를 변경할 수 있다.")
    @Test
    void changeStatus() {
        // given
        order.setOrderStatus(OrderStatus.MEAL.name());
        when(orderDao.findById(order.getId())).thenReturn(Optional.of(order));

        Order updateOrder = new Order();
        updateOrder.setOrderStatus(OrderStatus.COMPLETION.name());

        // when
        Order actual = orderService.changeOrderStatus(this.order.getId(), updateOrder);

        // then
        assertThat(actual.getOrderStatus()).isEqualTo(updateOrder.getOrderStatus());
    }

    @DisplayName("주문의 상태가 이미 완료된 경우 변경할 수 없다.")
    @Test
    void complete() {
        // given
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        when(orderDao.findById(order.getId())).thenReturn(Optional.of(order));

        Order updateOrder = new Order();
        updateOrder.setOrderStatus(OrderStatus.MEAL.name());

        // when / then
        assertThrows(IllegalArgumentException.class, () -> orderService.changeOrderStatus(this.order.getId(), updateOrder));
    }
}
