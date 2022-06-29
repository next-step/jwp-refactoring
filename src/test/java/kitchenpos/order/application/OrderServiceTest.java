package kitchenpos.order.application;

import kitchenpos.application.OrderService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.menu.service.MenuServiceTest.createMenu01;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    private OrderService orderService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderLineItemRepository orderLineItemRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(menuRepository, orderRepository, orderLineItemRepository, orderTableRepository);
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        when(menuRepository.findAllById(any())).thenReturn(Arrays.asList(new Menu(1L)));
        OrderTable orderTable = new OrderTable(1L, 3, false);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(orderTable));
        when(orderRepository.save(any())).thenReturn(createOrder());

        // when
        Order created = orderService.create(createOrder());

        // then
        assertThat(created).isNotNull();
    }

    @DisplayName("[예외] 주문 항목이 없는 주문을 생성한다.")
    @Test
    void createOrder_without_order_list_item() {
        // when, then
        assertThatThrownBy(() -> {
            orderService.create(createOrderWithoutOrderListItem());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 메뉴와 메뉴 항목이 일치하지 않는 주문을 생성한다.")
    @Test
    void createOrder_menu_and_order_list_item_not_matching() {
        when(menuRepository.findAllById(any())).thenReturn(Arrays.asList(new Menu(1L), new Menu(2L)));

        // when, then
        assertThatThrownBy(() -> {
            orderService.create(createOrder());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 주문 테이블 없는 주문을 생성한다.")
    @Test
    void createOrder_without_order_table() {
        when(menuRepository.findAllById(any())).thenReturn(Arrays.asList(new Menu(1L)));
        when(orderTableRepository.findById(any())).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> {
            orderService.create(createOrder());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 비어 있는 주문 테이블에서 주문을 생성한다.")
    @Test
    void createOrder_with_empty_order_table() {
        when(menuRepository.findAllById(any())).thenReturn(Arrays.asList(new Menu(1L)));
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(new OrderTable(1L, 3, true)));

        // when, then
        assertThatThrownBy(() -> {
            orderService.create(createOrder());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() {
        when(orderRepository.findAll()).thenReturn(createOrderList());

        // when
        List<Order> list = orderService.list();

        // then
        assertThat(list).isNotNull();
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        when(orderRepository.findById(any())).thenReturn(Optional.of(createOrder()));
        when(orderRepository.save(any())).thenReturn(createOrder());

        // when
        Order changeOrder = new Order(OrderStatus.COMPLETION);
        Order updated = orderService.changeOrderStatus(1L, changeOrder);

        // then
        assertThat(updated).isNotNull();
        assertThat(updated.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @DisplayName("[예외] 저장되지 않은 주문의 상태를 변경한다.")
    @Test
    void changeOrderStatus_with_not_saved_order() {
        when(orderRepository.findById(any())).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> {
            Order changeOrder = new Order(OrderStatus.COMPLETION);
            orderService.changeOrderStatus(1L, changeOrder);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 계산 완료 상태에서 추가로 주문 상태를 변경한다.")
    @Test
    void changeOrderStatus_with_completion_state() {
        when(orderRepository.findById(any())).thenReturn(Optional.of(createOrderWithCompletion()));

        // when, then
        assertThatThrownBy(() -> {
            Order changeOrder = new Order(OrderStatus.COMPLETION);
            orderService.changeOrderStatus(1L, changeOrder);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    public static Order createOrder() {
        OrderLineItem orderListItem = createOrderListItem();
        OrderTable orderTable = new OrderTable(1L, 3, false);
        return new Order(1L, orderTable, Arrays.asList(orderListItem));
    }

    public static Order createOrderWithoutOrderListItem() {
        OrderTable orderTable = new OrderTable(1L, 3, false);
        return new Order(1L, orderTable, null);
    }

    public static Order createOrderWithCompletion() {
        OrderLineItem orderListItem = createOrderListItem();
        OrderTable orderTable = new OrderTable(1L, 3, false);
        return new Order(1L, orderTable, OrderStatus.COMPLETION, Arrays.asList(orderListItem));
    }

    public static OrderLineItem createOrderListItem() {
        Menu menu = createMenu01();
        return new OrderLineItem(1L, new Order(1L), menu, 1);
    }

    public static List<Order> createOrderList() {
        return Collections.singletonList(createOrder());
    }

}
