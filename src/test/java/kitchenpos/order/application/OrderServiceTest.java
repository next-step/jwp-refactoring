package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.applicaiton.OrderService;
import kitchenpos.order.applicaiton.OrderTableService;
import kitchenpos.order.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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
    private MenuRepository menuRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableService orderTableService;
    @Mock
    private OrderLineItemRepository orderLineItemRepository;
    @InjectMocks
    private OrderService orderService;
    private OrderLineItem orderLineItem;
    private Order order;

    private Long orderId = 1L;
    private Long menuId = 1L;

    private Long tableGroupId = 1L;
    private Long orderTableId = 1L;
    private Long emptyOrderTableId = 1L;
    private int numberOfGuests = 4;
    private TableGroup tableGroup;
    private OrderTable orderTable;

    private OrderTable emptyOrderTable;

    @BeforeEach
    void setUp() {
        tableGroup = TableGroup.of(tableGroupId, LocalDateTime.now());
        emptyOrderTable = OrderTable.of(emptyOrderTableId,null, numberOfGuests, true);
        orderTable = OrderTable.of(orderTableId,null, numberOfGuests, false);
        order = getOrder(orderTable, OrderStatus.COOKING.name(), LocalDateTime.now());
        orderLineItem = OrderLineItem.of(order, Menu.of(menuId, "menu", BigDecimal.valueOf(1000)));
        order.addOrderLineItem(orderLineItem);
    }
    @Test
    @DisplayName("주문 시 주문 항목이 비어있으면 Exception")
    public void createEmptyException() {
        Order emptyOrder = getEmptyOrder(emptyOrderTable, OrderStatus.COOKING.name(), LocalDateTime.now());

        assertThatThrownBy(() -> orderService.create(emptyOrder)).isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    @DisplayName("주문 시 주문 항목이 메뉴에 존재하지 않으면 Exception")
    public void createOrderLineItemsNotExistsException() {
        final int menuCountById = 2;
        given(menuRepository.countByIdIn(any(List.class))).willReturn(menuCountById);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 존재하지 않을 경우 Exception")
    public void createTableNotExistsException() {
        final int menuCountById = 1;
        given(menuRepository.countByIdIn(any(List.class))).willReturn(menuCountById);
        given(orderTableService.findOrderTable(order.getOrderTableId())).willThrow(IllegalArgumentException.class);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 비어있을 경우 Exception")
    public void createEmptyTableException() {
        OrderTable orderTable = getOrderTable(orderTableId, tableGroup, numberOfGuests, true);
        final int menuCountById = 1;
        given(menuRepository.countByIdIn(any(List.class))).willReturn(menuCountById);
        given(orderTableService.findOrderTable(order.getOrderTableId())).willReturn(emptyOrderTable);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    private OrderTable getOrderTable(long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return OrderTable.of(id, tableGroup, numberOfGuests, empty);
    }

    @Test
    @DisplayName("주문 생성")
    public void create() {
        final int menuCountById = 1;
        given(menuRepository.countByIdIn(any(List.class))).willReturn(menuCountById);
        given(orderTableService.findOrderTable(order.getOrderTableId())).willReturn(orderTable);
        given(orderRepository.save(order)).willReturn(order);

        Order createdOrder = orderService.create(order);
        assertThat(createdOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    @DisplayName("주문 조회")
    public void list() {
        given(orderRepository.findAll()).willReturn(Arrays.asList(order));

        assertThat(orderService.list()).contains(order);
    }

    @Test
    @DisplayName("주문 변경 시 존재하지 않는 주문이면 Exception")
    public void changeOrderStatusNotExistsException() {
        Order changedOrder = getOrder(orderTable, OrderStatus.MEAL.name(), LocalDateTime.now(), orderLineItem);

        given(orderRepository.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, changedOrder)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 변경 시 완료상태일 경우 Exception")
    public void changeOrderStatusCompletionException() {
        Order completedOrder = getOrder(orderTable, OrderStatus.COMPLETION.name(), LocalDateTime.now(), orderLineItem);
        Order changedOrder = getOrder(orderTable, OrderStatus.MEAL.name(), LocalDateTime.now(), orderLineItem);

        given(orderRepository.findById(completedOrder.getId())).willReturn(Optional.of(completedOrder));

        assertThatThrownBy(() -> orderService.changeOrderStatus(completedOrder.getId(), changedOrder)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 변경")
    public void changeOrderStatus() {
        Order changedOrder = getOrder(orderTable, OrderStatus.MEAL.name(), LocalDateTime.now(), orderLineItem);

        given(orderRepository.findById(order.getId())).willReturn(Optional.of(order));

        assertThat(orderService.changeOrderStatus(order.getId(), changedOrder).getOrderStatus()).isEqualTo(
                changedOrder.getOrderStatus());
    }

    private Order getOrder(OrderTable orderTable, String status, LocalDateTime orderedTime) {
        return Order.of(orderTable, status, orderedTime, null);
    }

    private Order getOrder(OrderTable orderTable, String status, LocalDateTime orderedTime, OrderLineItem orderLineItem) {
        return Order.of(orderTable, status, orderedTime, Arrays.asList(orderLineItem));
    }

    private Order getEmptyOrder(OrderTable orderTable, String orderStatus, LocalDateTime orderedTime) {
        return Order.of(orderTable, orderStatus, orderedTime);
    }
}