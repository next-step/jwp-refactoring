package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.applicaiton.OrderService;
import kitchenpos.order.applicaiton.OrderTableService;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
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
    private OrderTable orderTable;
    private OrderTable emptyOrderTable;
    private OrderRequest orderRequestFromEmptyOrderTable;
    private OrderRequest orderRequestFromOrderTable;
    private Menu menu;

    @BeforeEach
    void setUp() {

        menu = Menu.of(menuId, "menu", BigDecimal.valueOf(1000));

        orderTable = OrderTable.of(orderTableId,null, numberOfGuests, false);
        order = getOrder(orderTable, OrderStatus.COOKING.name(), LocalDateTime.now());
        orderLineItem = OrderLineItem.of(order, menu);
        order.addOrderLineItem(orderLineItem);
        orderRequestFromOrderTable = OrderRequest.of(orderTableId, Arrays.asList(orderLineItem));

        emptyOrderTable = OrderTable.of(emptyOrderTableId,null, numberOfGuests, true);
        orderRequestFromEmptyOrderTable = OrderRequest.of(emptyOrderTableId);


    }
    @Test
    @DisplayName("주문 시 주문 테이블이 비어있으면 Exception")
    public void createEmptyException() {
        assertThatThrownBy(() -> orderService.create(orderRequestFromEmptyOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    @DisplayName("주문 시 주문 항목이 메뉴에 존재하지 않으면 Exception")
    public void createOrderLineItemsNotExistsException() {
        given(menuRepository.findAllById(any()))
                .willReturn(Arrays.asList(menu, Menu.of(2L,"요청에없는메뉴",BigDecimal.valueOf(2000))));

        assertThatThrownBy(() -> orderService.create(orderRequestFromOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 존재하지 않을 경우 Exception")
    public void createTableNotExistsException() {
        given(orderTableService.findOrderTable(order.getOrderTableId())).willThrow(IllegalArgumentException.class);

        assertThatThrownBy(() -> orderService.create(orderRequestFromOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 비어있을 경우 Exception")
    public void createEmptyTableException() {
        given(orderTableService.findOrderTable(order.getOrderTableId())).willReturn(emptyOrderTable);

        assertThatThrownBy(() -> orderService.create(orderRequestFromEmptyOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 생성")
    public void create() {
        given(menuRepository.findAllById(any())).willReturn(Arrays.asList(menu));
        given(orderTableService.findOrderTable(order.getOrderTableId())).willReturn(orderTable);
        given(orderRepository.save(any())).willReturn(order);

        OrderResponse response = orderService.create(orderRequestFromOrderTable);
        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @Test
    @DisplayName("주문 조회")
    public void list() {
        given(orderRepository.findAll()).willReturn(Arrays.asList(order));

        assertThat(orderService.list()).contains(OrderResponse.of(order));
    }

    @Test
    @DisplayName("주문 변경 시 존재하지 않는 주문이면 Exception")
    public void changeOrderStatusNotExistsException() {
        Order changedOrder = getOrder(orderTable, OrderStatus.MEAL.name(), LocalDateTime.now(), orderLineItem);

        given(orderRepository.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, OrderStatusRequest.of(changedOrder
                .getOrderStatus()))).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 변경 시 완료상태일 경우 Exception")
    public void changeOrderStatusCompletionException() {
        Order completedOrder = getOrder(orderTable, OrderStatus.COMPLETION.name(), LocalDateTime.now(), orderLineItem);

        given(orderRepository.findById(completedOrder.getId())).willReturn(Optional.of(completedOrder));

        assertThatThrownBy(() -> orderService.changeOrderStatus(completedOrder.getId(), OrderStatusRequest.of(completedOrder
                .getOrderStatus()))).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 변경")
    public void changeOrderStatus() {
        Order changedOrder = getOrder(orderTable, OrderStatus.MEAL.name(), LocalDateTime.now(), orderLineItem);

        given(orderRepository.findById(order.getId())).willReturn(Optional.of(order));

        assertThat(orderService.changeOrderStatus(order.getId(), OrderStatusRequest.of(changedOrder.getOrderStatus())).getOrderStatus()).isEqualTo(
                changedOrder.getOrderStatus());
    }

    private Order getOrder(OrderTable orderTable, String status, LocalDateTime orderedTime) {
        return Order.of(orderTable, status, orderedTime, null);
    }

    private Order getOrder(OrderTable orderTable, String status, LocalDateTime orderedTime, OrderLineItem orderLineItem) {
        return Order.of(orderTable, status, orderedTime, Arrays.asList(orderLineItem));
    }
}