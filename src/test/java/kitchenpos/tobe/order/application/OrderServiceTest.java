package kitchenpos.tobe.order.application;

import kitchenpos.tobe.menu.application.MenuNotMatchException;
import kitchenpos.tobe.menu.domain.Menu;
import kitchenpos.tobe.menu.domain.MenuRepository;
import kitchenpos.tobe.order.domain.Order;
import kitchenpos.tobe.order.domain.OrderLineItems;
import kitchenpos.tobe.order.domain.OrderRepository;
import kitchenpos.tobe.order.domain.OrderStatus;
import kitchenpos.tobe.order.dto.OrderLineItemRequest;
import kitchenpos.tobe.order.dto.OrderRequest;
import kitchenpos.tobe.order.dto.OrderResponse;
import kitchenpos.tobe.table.application.OrderTableNotFoundException;
import kitchenpos.tobe.table.domain.OrderTable;
import kitchenpos.tobe.table.domain.OrderTableRepository;
import kitchenpos.tobe.table.domain.OrderTables;
import kitchenpos.tobe.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private MenuRepository menuRepository;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository, orderTableRepository, menuRepository);
    }

    @DisplayName("주어진 주문을 저장하고, 저장된 객체를 리턴한다.")
    @Test
    void create_order() {
        OrderTable givenOrderTable = new OrderTable(1L, null, 2, false);
        Order givenOrder = Order.builder()
                .orderTable(givenOrderTable)
                .orderLineItems(new OrderLineItems())
                .orderStatus(OrderStatus.COOKING)
                .build();

        OrderLineItemRequest orderLineItemRequest1 = new OrderLineItemRequest(1L, 2L);
        OrderLineItemRequest orderLineItemRequest2 = new OrderLineItemRequest(2L, 3L);
        OrderRequest orderRequest = new OrderRequest(1L, Arrays.asList(orderLineItemRequest1, orderLineItemRequest2));
        Menu menu1 = Menu.builder().builder();
        Menu menu2 = Menu.builder().builder();

        when(menuRepository.findAllById(anyList()))
                .thenReturn(Arrays.asList(menu1, menu2));
        when(orderTableRepository.findById(anyLong()))
                .thenReturn(Optional.of(givenOrderTable));
        when(orderRepository.save(any(Order.class)))
                .thenReturn(givenOrder);

        OrderResponse actual = orderService.create(orderRequest);

        assertThat(actual).isNotNull();
    }

    @DisplayName("주문 저장시 비어있는 주문 항목 목록이 주어지면 예외를 던진다.")
    @Test
    void create_order_with_empty_order_lines() {
        OrderRequest orderRequest = new OrderRequest(1L, new ArrayList<>());

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(OrderTableNotFoundException.class);
    }

    @DisplayName("주문 저장시 주문항목 갯수와 메뉴의 갯수가 다르게 주어지면 예외를 던진다.")
    @Test
    void create_with_different_menu_size() {
        OrderLineItemRequest orderLineItemRequest1 = new OrderLineItemRequest(1L, 2L);
        OrderLineItemRequest orderLineItemRequest2 = new OrderLineItemRequest(2L, 3L);
        OrderRequest orderRequest = new OrderRequest(1L, Arrays.asList(orderLineItemRequest1, orderLineItemRequest2));
        OrderTable orderTable = new OrderTable(1L, null, 4, false);
        OrderTable orderTable2 = new OrderTable(1L, null, 4, false);
        TableGroup tableGroup = TableGroup.of(new OrderTables(Arrays.asList(orderTable, orderTable2)));
        OrderTable givenOrderTable = new OrderTable(1L, tableGroup, 5, false);
        Menu menu1 = Menu.builder().builder();

        when(menuRepository.findAllById(anyList()))
                .thenReturn(Arrays.asList(menu1));
        when(orderTableRepository.findById(anyLong()))
                .thenReturn(Optional.of(givenOrderTable));

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(MenuNotMatchException.class);
    }

    @DisplayName("주문 저장시 주문 테이블이 존재하지 않으면 예외를 던진다.")
    @Test
    void create_order_with_not_empty_order_table() {
        OrderRequest orderRequest = new OrderRequest(1L, new ArrayList<>());

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(OrderTableNotFoundException.class);
    }

    @DisplayName("주문이 주문 테이블이 비어있는 상태로 주어지면 예외를 던진다.")
    @Test
    void create_order_with_empty_order_table() {
        OrderTable givenOrderTable = new OrderTable(1L, null, 2, true);
        OrderLineItemRequest orderLineItemRequest1 = new OrderLineItemRequest(1L, 2L);
        OrderLineItemRequest orderLineItemRequest2 = new OrderLineItemRequest(2L, 3L);
        OrderRequest orderRequest = new OrderRequest(1L, Arrays.asList(orderLineItemRequest1, orderLineItemRequest2));
        Menu menu1 = Menu.builder().builder();
        Menu menu2 = Menu.builder().builder();

        when(menuRepository.findAllById(anyList()))
                .thenReturn(Arrays.asList(menu1, menu2));
        when(orderTableRepository.findById(anyLong()))
                .thenReturn(Optional.of(givenOrderTable));

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("모든 주문을 조회한다")
    void list() {
        Order order1 = Order.builder()
                .orderLineItems(new OrderLineItems())
                .orderStatus(OrderStatus.COOKING)
                .build();
        Order order2 = Order.builder()
                .orderLineItems(new OrderLineItems())
                .orderStatus(OrderStatus.COOKING)
                .build();

        when(orderRepository.findAll())
                .thenReturn(Arrays.asList(order1, order2));
        List<OrderResponse> orders = orderService.list();

        assertThat(orders).containsAll(Arrays.asList(new OrderResponse(order1), new OrderResponse(order2)));
    }

    @DisplayName("주문의 상태를 변경한다.")
    @Test
    void changeStatus() {
        Order givenOrder = Order.builder()
                .id(1L)
                .orderLineItems(new OrderLineItems())
                .orderStatus(OrderStatus.COOKING)
                .build();
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(givenOrder));

        OrderResponse actual = orderService.changeOrderStatus(1L, OrderStatus.MEAL);

        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("주문완료 상태 변경시 예외를 던진다.")
    @Test
    void changeStatusWithOrderComplete() {
        Order givenOrder = Order.builder()
                .id(1L)
                .orderLineItems(new OrderLineItems())
                .orderStatus(OrderStatus.COMPLETION)
                .build();
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(givenOrder));

        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, OrderStatus.MEAL))
                .isInstanceOf(IllegalStateException.class);
    }
}
