package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.exception.MenuNotFoundException;
import kitchenpos.repos.MenuRepository;
import kitchenpos.repos.OrderRepository;
import kitchenpos.repos.OrderTableRepository;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    MenuRepository menuRepository;

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderLineItemDao orderLineItemDao;

    @Mock
    OrderTableRepository orderTableRepository;

    @InjectMocks
    OrderService orderService;

    private OrderLineItem orderLineItem;

    private OrderTable orderTable;

    private Order order;

    private Order order2;

    private TableGroup tableGroup;

    private Menu menu;

    private OrderRequest orderRequest;

    private OrderLineItemRequest orderLineItemRequest;

    @BeforeEach
    void setUp() {
        tableGroup = TableGroup.of(1L, null, null);
        orderTable = OrderTable.of(1L, tableGroup, 2, false);
        orderLineItem = OrderLineItem.of(1L, null, null, 1);
        order = Order.of(1L, orderTable, null, null, Arrays.asList(orderLineItem));
        order2 = Order.of(2L, orderTable, null, null, Arrays.asList(orderLineItem));
        menu = Menu.of(1L, "메뉴", BigDecimal.TEN, null, null);
        orderLineItemRequest = new OrderLineItemRequest(1L, 1);
        orderRequest = new OrderRequest(1L, OrderStatus.MEAL, Arrays.asList(orderLineItemRequest));
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void create() {
        // given
        when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
        when(orderRepository.save(any())).thenReturn(order);

        //when
        Order expected = orderService.create(orderRequest);

        //then
        assertThat(order.getId()).isEqualTo(expected.getId());
    }

    @DisplayName("메뉴에 없는 주문 항목이 있으면 주문을 등록할 수 없다.")
    @Test
    void create2() {
        // given
        when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));

        //then
        assertThatThrownBy(
                () -> orderService.create(orderRequest)
        ).isInstanceOf(MenuNotFoundException.class);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() {
        // given
        List<Order> actual = Arrays.asList(order, order2);
        when(orderRepository.findAll()).thenReturn(actual);

        //when
        List<Order> expected = orderService.list();

        //then
        assertThat(actual.size()).isEqualTo(expected.size());
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // given
        Order 조리중_주문 = Order.of(1L, orderTable, OrderStatus.COOKING, LocalDateTime.now(), Arrays.asList(orderLineItem));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(조리중_주문));

        //when
        Order expected = orderService.changeOrderStatus(order.getId(), orderRequest);

        //then
        assertThat(OrderStatus.MEAL.name()).isEqualTo(expected.getOrderStatus());
    }
}
