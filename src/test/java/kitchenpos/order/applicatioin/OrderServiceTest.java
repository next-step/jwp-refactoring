package kitchenpos.order.applicatioin;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        orderTable = new OrderTable(1L, 4, false);
        orderTable.setId(1L);
        orderLineItem = new OrderLineItem(1L, 1L);

        order = new Order(1L, OrderStatus.COOKING.name());
        order.setOrderLineItems(Arrays.asList(orderLineItem));
    }

    @Test
    @DisplayName("주문 등록")
    void create() {
        when(menuRepository.countByIdIn(any())).thenReturn(1);
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
        order.changeOrderTableId(2L);
        when(orderRepository.findById(any())).thenReturn(Optional.of(order));
        when(orderRepository.save(any())).thenReturn(order);

        assertThat(orderService.changeOrderStatus(order.getId(), order));
    }

    @Test
    @DisplayName("주문 수정시 주문상태가 완료이면 수정할 수 없음")
    void callExceptionChangeOrderStatus() {
        order.changeOrderStatus(OrderStatus.COMPLETION.name());
        when(orderRepository.findById(any())).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(order.getId(), order);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
