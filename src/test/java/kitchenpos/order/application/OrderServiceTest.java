package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import kitchenpos.menu.infrastructure.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderUpdateRequest;
import kitchenpos.order.infrastructure.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.infrastructure.OrderTableRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("주문 상품이 없는 경우, 예외를 반환한다.")
    void createFail() {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(null, 1L);
        OrderRequest orderRequest = new OrderRequest(1L, Lists.list(orderLineItemRequest));
        //when,then
        assertThatThrownBy(() -> {
            orderService.create(orderRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 상품의 메뉴가 존재하지 않는 경우, 예외를 반환한다.")
    void createWithNoExistingMenu() {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 1L);
        OrderRequest orderRequest = new OrderRequest(1L, Lists.list(orderLineItemRequest));
        when(menuRepository.countByIdIn(any())).thenReturn(0);

        assertThatThrownBy(() -> {
            orderService.create(orderRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 존재하지 않으면 예외를 반환한다.")
    void createWithNoExistingOrderTable() {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 1L);
        OrderRequest orderRequest = new OrderRequest(1L, Lists.list(orderLineItemRequest));
        when(menuRepository.countByIdIn(any())).thenReturn(1);
        when(orderTableRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            orderService.create(orderRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 빈 테이블이면 예외를 반환한다.")
    void createWithEmptyOrderTable() {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 1L);
        OrderRequest orderRequest = new OrderRequest(1L, Lists.list(orderLineItemRequest));
        OrderTable orderTable = OrderTable.of(4, true);
        when(menuRepository.countByIdIn(any())).thenReturn(1);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> {
            orderService.create(orderRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문을 생성한다.")
    void create() {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 1L);
        OrderRequest orderRequest = new OrderRequest(1L, Lists.list(orderLineItemRequest));
        OrderTable orderTable = OrderTable.of(4, false);
        Order order = Order.from(orderTable);
        when(menuRepository.countByIdIn(any())).thenReturn(1);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(orderTable));
        when(orderRepository.save(any())).thenReturn(order);

        OrderResponse saved = orderService.create(orderRequest);

        assertThat(saved.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    @DisplayName("주문 목록을 조회한다.")
    void findAll() {
        OrderTable orderTable = OrderTable.of(4, false);
        Order order = Order.from(orderTable);
        when(orderRepository.findAll()).thenReturn(Lists.list(order));

        List<OrderResponse> actual = orderService.list();

        assertThat(actual).hasSize(1);
    }

    @Test
    @DisplayName("주문이 존재하지 않으면 주문 상태를 변경할 수 없다.")
    void changeOrderStatusWithNoExistingOrder() {
        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(99L, new OrderUpdateRequest());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문상태가 계산완료인경우, 주문 상태를 변경할 수 없다.")
    void changeOrderStatusWithCompletion() {
        OrderTable orderTable = OrderTable.of(4, false);
        Order order = Order.from(orderTable);
        order.updateOrderStatus(OrderStatus.COMPLETION);
        when(orderRepository.findById(any())).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(1L, new OrderUpdateRequest());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 상태를 변경한다.")
    void changeOrderStatus() {
        OrderUpdateRequest orderUpdateRequest = new OrderUpdateRequest(OrderStatus.MEAL);
        OrderTable orderTable = OrderTable.of(4, false);
        OrderLineItem orderLineItem = OrderLineItem.createOrderLineItem(1L, 1L);
        Order order = Order.from(orderTable);
        order.addOrderLineItems(Lists.list(orderLineItem));
        when(orderRepository.findById(any())).thenReturn(Optional.of(order));
        when(orderRepository.save(any())).thenReturn(order);

        OrderResponse changed = orderService.changeOrderStatus(1L, orderUpdateRequest);

        assertThat(changed.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }
}
