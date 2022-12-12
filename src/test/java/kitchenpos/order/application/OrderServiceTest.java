package kitchenpos.order.application;

import kitchenpos.menu.dao.MenuDao;
import kitchenpos.order.applicaiton.OrderService;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private MenuDao menuDao;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private OrderLineItemDao orderLineItemDao;
    @InjectMocks
    private OrderService orderService;
    private OrderLineItem orderLineItem;
    private Order order;

    private Long orderId = 1L;
    private Long menuId = 1L;
    private Long orderTableId = 1L;
    private Long tableGroupId = 1L;

    private int numberOfGuests = 4;

    @BeforeEach
    void setUp() {
        orderLineItem = getOrderLineItem(orderId, menuId);
        order = getOrder(orderTableId, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItem);
    }

    private OrderLineItem getOrderLineItem(long orderId, long menuId) {
        return OrderLineItem.of(orderId, menuId);
    }

    @Test
    @DisplayName("주문 시 주문 항목이 비어있으면 Exception")
    public void createEmptyException() {
        Long emptyOrderTableId = 2L;
        Order emptyOrder = getEmptyOrder(emptyOrderTableId, OrderStatus.COOKING.name(), LocalDateTime.now());

        assertThatThrownBy(() -> orderService.create(emptyOrder)).isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    @DisplayName("주문 시 주문 항목이 메뉴에 존재하지 않으면 Exception")
    public void createOrderLineItemsNotExistsException() {
        final Long menuCountById = 2L;
        given(menuDao.countByIdIn(any(List.class))).willReturn(menuCountById);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 존재하지 않을 경우 Exception")
    public void createTableNotExistsException() {
        final Long menuCountById = 1L;
        given(menuDao.countByIdIn(any(List.class))).willReturn(menuCountById);
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 비어있을 경우 Exception")
    public void createEmptyTableException() {
        OrderTable orderTable = getOrderTable(orderTableId, tableGroupId, numberOfGuests, true);
        final Long menuCountById = 1L;
        given(menuDao.countByIdIn(any(List.class))).willReturn(menuCountById);
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    private OrderTable getOrderTable(long id, long tableGroupId, int numberOfGuests, boolean empty) {
        return OrderTable.of(id, tableGroupId, numberOfGuests, empty);
    }

    @Test
    @DisplayName("주문 생성")
    public void create() {
        OrderTable orderTable = getOrderTable(orderTableId, tableGroupId, numberOfGuests, false);

        final Long menuCountById = 1L;
        given(menuDao.countByIdIn(any(List.class))).willReturn(menuCountById);
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(orderTable));
        given(orderDao.save(order)).willReturn(order);
        given(orderLineItemDao.save(orderLineItem)).willReturn(orderLineItem);

        Order createdOrder = orderService.create(order);
        assertThat(createdOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    @DisplayName("주문 조회")
    public void list() {
        given(orderDao.findAll()).willReturn(Arrays.asList(order));

        assertThat(orderService.list()).contains(order);
    }

    @Test
    @DisplayName("주문 변경 시 존재하지 않는 주문이면 Exception")
    public void changeOrderStatusNotExistsException() {
        Order changedOrder = getOrder(orderTableId, OrderStatus.MEAL.name(), LocalDateTime.now(), orderLineItem);

        given(orderDao.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, changedOrder)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 변경 시 완료상태일 경우 Exception")
    public void changeOrderStatusCompletionException() {
        Order completedOrder = getOrder(orderTableId, OrderStatus.COMPLETION.name(), LocalDateTime.now(), orderLineItem);
        Order changedOrder = getOrder(orderTableId, OrderStatus.MEAL.name(), LocalDateTime.now(), orderLineItem);

        given(orderDao.findById(completedOrder.getId())).willReturn(Optional.of(completedOrder));

        assertThatThrownBy(() -> orderService.changeOrderStatus(completedOrder.getId(), changedOrder)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 변경")
    public void changeOrderStatus() {
        Order changedOrder = getOrder(orderTableId, OrderStatus.MEAL.name(), LocalDateTime.now(), orderLineItem);

        given(orderDao.findById(order.getId())).willReturn(Optional.of(order));

        assertThat(orderService.changeOrderStatus(order.getId(), changedOrder).getOrderStatus()).isEqualTo(
                changedOrder.getOrderStatus());
    }

    private Order getOrder(long orderTableId, String status, LocalDateTime orderedTime, OrderLineItem orderLineItem) {
        return Order.of(orderTableId, status, orderedTime, Arrays.asList(orderLineItem));
    }

    private Order getEmptyOrder(long orderTableId, String orderStatus, LocalDateTime orderedTime) {
        return Order.of(orderTableId, orderStatus, orderedTime);
    }
}