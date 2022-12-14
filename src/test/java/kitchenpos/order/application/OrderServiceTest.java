package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
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
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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
        doReturn(Optional.ofNullable(OrderTable.builder().build()))
                .when(orderTableRepository).findById(orderRequest.getOrderTableId());
        doReturn(Arrays.asList(Menu.builder().price(BigDecimal.valueOf(1000)).build(), Menu.builder().price(BigDecimal.valueOf(1000)).build()))
                .when(menuRepository).findAllById(anyList());

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 추가할 경우 주문테이블이 등록안되있으면 예외발생")
    @Test
    public void throwsExceptionWhenNoneExistsTable() {
        OrderRequest order = new OrderRequest();
        doReturn(Optional.empty())
                .when(orderTableRepository).findById(order.getOrderTableId());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 추가할 경우 주문테이블이 공석이면 예외발생")
    @Test
    public void throwsExceptionWhenEmptyTable() {
        OrderRequest order = new OrderRequest();
        order.setOrderTableId(15l);
        order.setOrderLineItems(Arrays.asList(new OrderLineItemRequest()));
        OrderTable orderTable = OrderTable.builder().empty(true).build();
        doReturn(Optional.ofNullable(orderTable))
                .when(orderTableRepository).findById(order.getOrderTableId());
        doReturn(Arrays.asList(Menu.builder().price(BigDecimal.valueOf(1000)).build()))
                .when(menuRepository).findAllById(anyList());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(OrderException.class)
                .hasMessageContaining("주문테이블이 존재하지 않습니다");
    }

    @DisplayName("주문을 추가할 경우 주문을 반환")
    @Test
    public void returnOrder() {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest();
        orderLineItemRequest.setMenuId(2l);
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderTableId(15l);
        orderRequest.setOrderLineItems(Arrays.asList(orderLineItemRequest));
        OrderTable orderTable = OrderTable.builder().id(12l).build();
        doReturn(Optional.ofNullable(orderTable))
                .when(orderTableRepository).findById(anyLong());
        doReturn(Arrays.asList(Menu.builder().price(BigDecimal.valueOf(1000)).id(2l).build()))
                .when(menuRepository).findAllById(anyList());
        doReturn(Order.builder()
                .id(13l)
                .orderTable(orderTable)
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
                .orderTable(OrderTable.builder().build())
                .orderLineItems(Arrays.asList(OrderLineItem.builder().menu(Menu.builder().price(BigDecimal.valueOf(1000)).build()).build())).build(), 30);
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
                .id(Arbitraries.longs().between(1, 1000).sample())
                .orderTable(OrderTable.builder().build())
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
                .id(Arbitraries.longs().between(1, 1000).sample())
                .orderTable(OrderTable.builder().build())
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
                        .orderTable(order.getOrderTable())
                        .orderStatus(order.getOrderStatus())
                        .orderLineItems(order.getOrderLineItems())
                        .build())
                .collect(Collectors.toList());
    }

    private List<OrderLineItemRequest> getOrderLineItems() {
        return IntStream.rangeClosed(1, 20)
                .mapToObj(value -> {
                    OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest();
                    orderLineItemRequest.setMenuId(Arbitraries.longs().between(1, 100).sample());
                    return orderLineItemRequest;
                })
                .collect(Collectors.toList());
    }
}
