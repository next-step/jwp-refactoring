package kitchenpos.order.applicatioin;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderLineItemRepository orderLineItemRepository;
    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private OrderLineItem orderLineItem;
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {

        Menu menu1 = new Menu("후라이드+후라이드", new BigDecimal(19000));
        Menu menu2 = new Menu("양념반+후라이드반", new BigDecimal(19000));
        Menu menu3 = new Menu("양념반+마늘반", new BigDecimal(19000));

        OrderLineItem orderLineItem1 = new OrderLineItem(1L, menu1, 3);
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, menu2, 2);
        OrderLineItem orderLineItem3 = new OrderLineItem(3L, menu3, 1);

        orderTable = new OrderTable(new TableGroup(), 4, true);
        orderTable.setId(1L);

        order = new Order(orderTable, OrderStatus.COOKING);
        order.setId(1L);
        order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2, orderLineItem3));

        orderLineItem = new OrderLineItem(order, 1);
    }

    @Test
    @DisplayName("주문 등록")
    void create() {
        when(menuRepository.countByIdIn(any())).thenReturn(3);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(orderTable));
        when(orderRepository.save(any())).thenReturn(order);

        assertThat(orderService.create(order)).isNotNull();
    }

    @Test
    @DisplayName("주문 등록시 주문항목이 메뉴의 개수와 같이 않으면 등록 안됨")
    void callException() {
        when(menuRepository.countByIdIn(any())).thenReturn(0);

        assertThatThrownBy(() -> {
            orderService.create(order);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 조회")
    void list() {
        when(orderRepository.findAll()).thenReturn(Arrays.asList(order));

        assertThat(orderService.list()).isNotNull();
    }

    @Test
    @DisplayName("주문 수정")
    void changeOrderStatus() {
        order.changeOrderTable(orderTable);
        when(orderRepository.findById(any())).thenReturn(Optional.of(order));
        when(orderRepository.save(any())).thenReturn(order);

        assertThat(orderService.changeOrderStatus(order.getId(), order));
    }

    @Test
    @DisplayName("주문 수정시 주문상태가 완료이면 수정할 수 없음")
    void callExceptionChangeOrderStatus() {
        order.changeOrderStatus(OrderStatus.COMPLETION);
        when(orderRepository.findById(any())).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(order.getId(), order);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
