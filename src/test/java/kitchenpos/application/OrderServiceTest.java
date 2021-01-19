package kitchenpos.application;



import kitchenpos.domain.*;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private OrderRequest orderRequest;
    private Order order;
    private Menu firstMenu;
    private Menu secondMenu;
    private OrderTable orderTable;
    private List<OrderLineItem> orderLineItems = new ArrayList<>();
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private OrderLineItemRepository orderLineItemRepository;

    OrderServiceTest() {
    }

    @BeforeEach
    public void setUp() {
        firstMenu = new Menu(1L);
        secondMenu = new Menu(2L);
        orderLineItems.add(new OrderLineItem(1L, firstMenu, 5));
        orderLineItems.add(new OrderLineItem(2L, secondMenu, 2));
        orderRequest = new OrderRequest(1L, orderLineItems);
        orderTable = new OrderTable(1L, 0, false);
        order = new Order (1L, OrderStatus.COOKING.name(), orderTable);
        orderService = new OrderService(orderRepository, orderLineItemRepository, orderTableRepository);
    }

    @DisplayName("주문 등록 테스트")
    @Test
    void createOrder() {
        when(orderTableRepository.findById(orderRequest.getOrderTableId())).thenReturn(java.util.Optional.ofNullable(orderTable));
        when(orderRepository.save(any())).thenReturn(order);
        when(orderLineItemRepository.save(orderLineItems.get(0))).thenReturn(orderLineItems.get(0));
        when(orderLineItemRepository.save(orderLineItems.get(1))).thenReturn(orderLineItems.get(1));

        OrderResponse resultOrder = orderService.create(orderRequest);

        assertAll(
                () -> assertThat(resultOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(resultOrder.getOrderTableResponse().getId())
                        .isEqualTo(1L)
        );
    }

    @DisplayName("주문 등록 예외테스트: 주문 테이블을 찾을 수 없는경우")
    @Test
    void notFoundOrderTable() {
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.create(orderRequest)
        );

        assertThat(exception.getMessage()).isEqualTo("주문 테이블을 찾을 수 없습니다.");
    }

    @DisplayName("주문 등록 예외테스트: 주문 테이블이 비어있는경우")
    @Test
    void emptyOrderTable() {
        when(orderTableRepository.findById(orderRequest.getOrderTableId())).thenReturn(java.util.Optional.ofNullable(orderTable));
        when(orderRepository.save(any())).thenReturn(order);
        when(orderLineItemRepository.save(orderLineItems.get(0))).thenReturn(orderLineItems.get(0));
        when(orderLineItemRepository.save(orderLineItems.get(1))).thenReturn(orderLineItems.get(1));

        orderTable.changeStatus(true);


        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.create(orderRequest)
        );

        assertThat(exception.getMessage()).isEqualTo("테이블이 비어있습니다.");
    }

    @DisplayName("주문목록 조회 테스트")
    @Test
    void findOrderList() {
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        when(orderRepository.findAll()).thenReturn(orders);

        List<OrderResponse> resultOrders = orderService.list();
        List<Long> orderIds =resultOrders.stream()
                .map(order -> order.getId())
                .collect(Collectors.toList());
        List<Long> expectedOrderIds =orders.stream()
                .map(order -> order.getId())
                .collect(Collectors.toList());

        assertThat(resultOrders.size()).isEqualTo(orders.size());
        assertThat(orderIds).containsExactlyElementsOf(expectedOrderIds);
    }

    @DisplayName("주문 상태변화 테스트")
    @Test
    void changeOrderStatus() {
        when(orderRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(order));

        OrderResponse resultOrder = orderService.changeOrderStatus(1L, OrderStatus.MEAL.name());

        assertThat(resultOrder.getOrderStatus()).isEqualTo(order.getOrderStatus());
    }

    @DisplayName("주문 상태변경 예외테스트: 주문을 찾을 수 없는 경우")
    @Test
    void notFoundOrder() {
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.changeOrderStatus(1L, OrderStatus.MEAL.name())
        );

        assertThat(exception.getMessage()).isEqualTo("주문을 찾을 수 없습니다.");
    }
    @DisplayName("주문 상태변경 예외테스트: 주문상태가 올바르지 않은 경우")
    @Test
    void invalidOrderStatus() {
        when(orderRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(order));
        order.changeOrderStatus(OrderStatus.COMPLETION);

        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.changeOrderStatus(1L, OrderStatus.MEAL.name())
        );

        assertThat(exception.getMessage()).isEqualTo("주문이 완료된 상태입니다.");
    }
}
