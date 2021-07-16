package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("주문 관련 기능 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    OrderLineItemRepository orderLineItemRepository;

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderTableRepository orderTableRepository;

    @Mock
    private ApplicationEventPublisher publisher;

    OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository, orderTableRepository, orderLineItemRepository, publisher);
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void create() {
        OrderRequest orderRequest = mock(OrderRequest.class);
        OrderLineItemRequest orderLineItemRequest = mock(OrderLineItemRequest.class);
        Menu menu = mock(Menu.class);
        Order order = mock(Order.class);
        OrderTable orderTable = mock(OrderTable.class);
        OrderLineItem orderLineItem = mock(OrderLineItem.class);

        when(orderRequest.toOrderLineItems()).thenReturn(Arrays.asList(orderLineItem));
        when(orderTableRepository.findById(any())).thenReturn(Optional.ofNullable(orderTable));
        when(order.getOrderStatus()).thenReturn(OrderStatus.COOKING);
        when(orderRepository.save(any())).thenReturn(order);

        OrderResponse orderResponse = 주문_생성_요청(orderRequest);

        주문_생성됨(orderResponse);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() {
        Order order = mock(Order.class);
        when(order.getId()).thenReturn(1L);
        when(order.getOrderStatus()).thenReturn(OrderStatus.MEAL);
        when(orderRepository.findAll()).thenReturn(Arrays.asList(order));

        List<OrderResponse> findOrders = 주문_목록_조회_요청();

        주문_목록_조회됨(findOrders);
    }

    @DisplayName("주문상태가 완료가 되면 변경 불가능하다.")
    @Test
    void changeOrderStatus_완료시_변경_불가() {
        Menu menu = mock(Menu.class);
        Order order = 주문_생성(1L, new OrderTable(1, false), OrderStatus.COMPLETION, Arrays.asList(new OrderLineItem(1L, menu.getId(), 2)));
        OrderRequest orderRequest = mock(OrderRequest.class);

        when(orderRequest.getOrderStatus()).thenReturn(OrderStatus.COMPLETION);
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        주문상태_완료시_변경_불가_예외_발생함(orderRequest);
    }

    @DisplayName("주문상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        Menu menu = mock(Menu.class);
        Order order = 주문_생성(1L, new OrderTable(1, false), OrderStatus.COOKING, Arrays.asList(new OrderLineItem(1L, menu.getId(), 2)));

        OrderRequest orderRequest = mock(OrderRequest.class);

        when(orderRequest.getOrderStatus()).thenReturn(OrderStatus.MEAL);
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        OrderResponse orderResponse = 주문_상태_변경_요청(orderRequest);

        주문_상태_변경됨(orderResponse);
    }

    private OrderResponse 주문_생성_요청(OrderRequest orderRequest) {
        return orderService.create(orderRequest);
    }

    private void 주문_생성됨(OrderResponse orderResponse) {
        assertThat(orderResponse).isNotNull();
    }

    private List<OrderResponse> 주문_목록_조회_요청() {
        return orderService.list();
    }

    private void 주문_목록_조회됨(List<OrderResponse> findOrders) {
        assertThat(findOrders.size()).isEqualTo(1);
        assertThat(findOrders.get(0).getId()).isEqualTo(1L);
    }

    private Order 주문_생성(Long id, OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        Menu menu = mock(Menu.class);
        return new Order(1L, orderTable.getId(), orderStatus);
    }

    private void 주문상태_완료시_변경_불가_예외_발생함(OrderRequest orderRequest) {
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("완료된 주문은 상태를 변경할 수 없습니다.");
    }

    private OrderResponse 주문_상태_변경_요청(OrderRequest orderRequest) {
        return orderService.changeOrderStatus(1L, orderRequest);
    }

    private void 주문_상태_변경됨(OrderResponse orderResponse) {
        assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }
}
