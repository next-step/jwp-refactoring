package kitchenpos.order.application;

import kitchenpos.menu.persistence.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.OrderException;
import kitchenpos.order.persistence.OrderLineItemRepository;
import kitchenpos.order.persistence.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.persistence.OrderTableRepository;
import kitchenpos.table.validator.OrderValidatorImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @InjectMocks
    private OrderService orderService;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderLineItemRepository orderLineItemRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private OrderValidatorImpl orderValidator;

    @DisplayName("주문을 추가할 경우 주문항목이 없으면 예외발생")
    @Test
    public void throwsExceptionWhenEmptyOrderItems() {
        OrderRequest order = new OrderRequest();
        order.setOrderLineItems(Collections.EMPTY_LIST);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 추가할 경우 등록되지 않는 메뉴가 있으면 예외발생")
    @Test
    public void throwsExceptionWhenNoneExistsMenu() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderTableId(13l);
        orderRequest.setOrderLineItems(Arrays.asList(new OrderLineItemRequest()));
        doThrow(new IllegalArgumentException())
                .when(orderValidator).validateOrderCreate(any(), anyList());

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 추가할 경우 주문테이블이 등록안되있으면 예외발생")
    @Test
    public void throwsExceptionWhenNoneExistsTable() {
        OrderRequest order = new OrderRequest();

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 추가할 경우 주문테이블이 공석이면 예외발생")
    @Test
    public void throwsExceptionWhenEmptyTable() {
        OrderRequest order = new OrderRequest();

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 추가할 경우 주문을 반환")
    @Test
    public void returnOrder() {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(2l, 3l);
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderTableId(15l);
        orderRequest.setOrderLineItems(Arrays.asList(orderLineItemRequest));
        OrderTable orderTable = OrderTable.builder().id(12l).build();
        doReturn(Order.builder()
                .id(13l)
                .orderTableId(orderTable.getId())
                .orderLineItems(Arrays.asList(OrderLineItem.builder().build()))
                .build())
                .when(orderRepository).save(any(Order.class));

        OrderResponse orderResponse = orderService.create(orderRequest);

        assertThat(orderResponse.getId()).isEqualTo(13l);
    }

    @DisplayName("주문목록을 조회할경우 주문목록 반환")
    @Test
    public void returnOrders() {
        List<Order> orders = getOrders(Order.builder().id(150l)
                .orderTableId(1l)
                .orderLineItems(Arrays.asList(OrderLineItem
                        .builder()
                        .menuId(1l)
                        .build())).build(), 30);
        doReturn(orders)
                .when(orderRepository).findAll();

        List<OrderResponse> findOrders = orderService.list();

        assertThat(findOrders).hasSize(30);
    }

    @DisplayName("주문상태를 수정할 경우 등록된 주문이 아니면 예외발생")
    @Test
    public void throwsExceptionWhenNoneExistsOrder() {
        doReturn(Optional.empty()).when(orderRepository).findById(anyLong());

        assertThatThrownBy(() -> orderService.changeOrderStatus(1l, OrderStatus.COOKING))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문상태를 수정할 경우 계산완료된 주문이면 예외발생")
    @Test
    public void throwsExceptionWhenCompleteOrder() {
        Order order = Order.builder()
                .id(3l)
                .orderTableId(1l)
                .orderStatus(OrderStatus.COMPLETION)
                .build();
        doReturn(Optional.ofNullable(order)).when(orderRepository).findById(order.getId());

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), OrderStatus.COMPLETION))
                .isInstanceOf(OrderException.class)
                .hasMessageContaining("계산이 완료된 주문은 상태를 변경할 수 없습니다");
    }

    @DisplayName("주문상태를 수정할 경우 수정된 주문반환")
    @Test
    public void returnOrderWithChangedStatus() {
        Order order = Order.builder()
                .id(2l)
                .orderTableId(1l)
                .orderLineItems(Arrays.asList(OrderLineItem.builder().build()))
                .orderStatus(OrderStatus.COOKING)
                .build();
        doReturn(Optional.ofNullable(order)).when(orderRepository).findById(order.getId());
        doReturn(order).when(orderRepository).save(any(Order.class));

        OrderResponse savedOrder = orderService.changeOrderStatus(order.getId(), OrderStatus.COMPLETION);

        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    private List<Order> getOrders(Order order, int size) {
        return IntStream.rangeClosed(1, size)
                .mapToObj(value -> Order.builder()
                        .id(order.getId())
                        .orderTableId(order.getOrderTableId())
                        .orderStatus(order.getOrderStatus())
                        .orderLineItems(order.getOrderLineItems())
                        .build())
                .collect(Collectors.toList());
    }
}
