package kitchenpos.order.application;

import static kitchenpos.order.application.TableServiceTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.repository.OrderLineItemRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.OrderTableRepository;

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

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(
            menuRepository, orderRepository, orderLineItemRepository, orderTableRepository);
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void createOrder() {
        // given
        Long orderId = 1L;
        OrderTable orderTable = 주문_테이블_생성(1L, 0, false);
        OrderLineItem orderLineItem = 주문_항목_생성(1L, 1);
        List<OrderLineItem> orderLineItems = Collections.singletonList(orderLineItem);
        List<Long> menuIds = orderLineItems.stream()
            .map(OrderLineItem::getMenuId)
            .collect(Collectors.toList());
        OrderRequest orderRequest = new OrderRequest(orderTable.getId(), orderLineItems.stream().map(
                orderLineItemRequest ->
                    new OrderLineItemRequest(orderLineItemRequest.getMenuId(), orderLineItemRequest.getQuantity()))
            .collect(Collectors.toList()));
        Order order = new Order(orderId, orderTable.getId(), OrderStatus.COOKING.name(), orderLineItems);

        given(menuRepository.countByIdIn(menuIds)).willReturn((long)order.getOrderLineItems().size());
        given(orderTableRepository.findById(order.getOrderTableId())).willReturn(Optional.of(orderTable));
        given(orderRepository.save(any())).willReturn(order);
        given(orderLineItemRepository.save(any())).willReturn(orderLineItem);

        // when
        OrderResponse savedOrder = orderService.create(orderRequest);

        // then
        assertEquals(OrderStatus.COOKING.name(), savedOrder.getOrderStatus());
        assertEquals(orderTable.getId(), savedOrder.getOrderTableId());
    }

    @DisplayName("주문 항목이 존재 해야 주문을 등록할 수 있다.")
    @Test
    void createOrderEmptyOrderLineItems() {
        // given
        Long orderId = 1L;
        OrderTable orderTable = 주문_테이블_생성(1L, 0, false);
        Order order = 주문_생성(orderId, orderTable.getId(), Collections.emptyList());
        OrderRequest orderRequest = new OrderRequest(order.getOrderTableId(), Collections.emptyList());

        // when && then
        assertThrows(IllegalArgumentException.class, () -> orderService.create(orderRequest));
        verify(menuRepository, times(0)).countByIdIn(Collections.emptyList());
    }

    @DisplayName("주문 항목과 해당하는 메뉴의 숫자가 일치해야 한다.")
    @Test
    void createOrderNotMenuCount() {
        // given
        Long orderId = 1L;
        OrderLineItem orderLineItem = 주문_항목_생성(1L, 1);
        OrderTable orderTable = 주문_테이블_생성(1L, 0, false);
        List<OrderLineItem> orderLineItems = Collections.singletonList(orderLineItem);
        List<Long> menuIds = orderLineItems.stream()
            .map(OrderLineItem::getMenuId)
            .collect(Collectors.toList());
        Order order = 주문_생성(orderId, orderTable.getId(), orderLineItems);

        OrderRequest orderRequest = new OrderRequest(order.getOrderTableId(), orderLineItems.stream().map(
                orderLineItemRequest ->
                    new OrderLineItemRequest(orderLineItemRequest.getMenuId(), orderLineItemRequest.getQuantity()))
            .collect(Collectors.toList()));

        given(menuRepository.countByIdIn(menuIds)).willReturn(0L);

        // when && then
        assertThrows(IllegalArgumentException.class, () -> orderService.create(orderRequest));
        verify(menuRepository).countByIdIn(menuIds);
        verify(orderTableRepository, times(0)).findById(order.getOrderTableId());
    }

    @DisplayName("주문 테이블이 존재해야 주문을 등록할 수 있다.")
    @Test
    void createOrderNotFoundOrderTable() {
        // given
        Long orderId = 1L;
        OrderLineItem orderLineItem = 주문_항목_생성(1L, 1);
        List<OrderLineItem> orderLineItems = Collections.singletonList(orderLineItem);
        List<Long> menuIds = orderLineItems.stream()
            .map(OrderLineItem::getMenuId)
            .collect(Collectors.toList());
        Order order = 주문_생성(orderId, null, Collections.singletonList(orderLineItem));

        OrderRequest orderRequest = new OrderRequest(order.getOrderTableId(), orderLineItems.stream().map(
                orderLineItemRequest ->
                    new OrderLineItemRequest(orderLineItemRequest.getMenuId(), orderLineItemRequest.getQuantity()))
            .collect(Collectors.toList()));

        given(menuRepository.countByIdIn(menuIds)).willReturn((long)menuIds.size());

        // when && then
        assertThrows(IllegalArgumentException.class, () -> orderService.create(orderRequest));
        verify(orderRepository, times(0)).save(order);
    }

    @DisplayName("주문 테이블이 빈 테이블이면 주문을 등록할 수 없다.")
    @Test
    void createOrderIsEmptyOrderTable() {
        // given
        Long orderId = 1L;
        OrderLineItem orderLineItem = 주문_항목_생성(1L, 1);
        OrderTable orderTable = 주문_테이블_생성(1L, 0, true);
        List<OrderLineItem> orderLineItems = Collections.singletonList(orderLineItem);
        List<Long> menuIds = orderLineItems.stream()
            .map(OrderLineItem::getMenuId)
            .collect(Collectors.toList());
        Order order = 주문_생성(orderId, orderTable.getId(), orderLineItems);

        OrderRequest orderRequest = new OrderRequest(order.getOrderTableId(), orderLineItems.stream().map(
                orderLineItemRequest ->
                    new OrderLineItemRequest(orderLineItemRequest.getMenuId(), orderLineItemRequest.getQuantity()))
            .collect(Collectors.toList()));

        given(menuRepository.countByIdIn(menuIds)).willReturn((long)order.getOrderLineItems().size());
        given(orderTableRepository.findById(order.getOrderTableId())).willReturn(Optional.of(orderTable));

        // when && then
        assertThrows(IllegalArgumentException.class, () -> orderService.create(orderRequest));
        verify(orderRepository, times(0)).save(order);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void getOrders() {
        // given
        Long orderId = 1L;
        OrderLineItem orderLineItem = 주문_항목_생성(1L, 1);
        List<OrderLineItem> orderLineItems = Collections.singletonList(orderLineItem);
        OrderTable orderTable = 주문_테이블_생성(1L, 0, false);
        Order order = 주문_생성(orderId, orderTable.getId(), orderLineItems);
        List<Order> orders = Collections.singletonList(order);

        given(orderRepository.findAll()).willReturn(orders);
        given(orderLineItemRepository.findAllByOrderId(orderId)).willReturn(orderLineItems);

        // when
        List<OrderResponse> findOrders = orderService.list();

        assertThat(findOrders).extracting("id")
            .containsExactlyElementsOf(orders.stream()
                .map(Order::getId).collect(Collectors.toList()));
    }

    @DisplayName("주문 상태를 갱신한다.")
    @Test
    void changeOrderStatus() {
        // given
        Long orderId = 1L;
        OrderLineItem orderLineItem = 주문_항목_생성(1L, 1);
        OrderTable orderTable = 주문_테이블_생성(1L, 0, false);
        Order order = 주문_생성(orderId, orderTable.getId(),
            OrderStatus.MEAL.name(), Collections.singletonList(orderLineItem));
        Order findOrder = 주문_생성(orderId, orderTable.getId(),
            OrderStatus.COOKING.name(), Collections.singletonList(orderLineItem));

        given(orderRepository.findById(orderId)).willReturn(Optional.of(findOrder));
        given(orderRepository.save(findOrder)).willReturn(null);
        given(orderLineItemRepository.findAllByOrderId(orderId))
            .willReturn(Collections.singletonList(orderLineItem));

        // when
        Order savedOrder = orderService.changeOrderStatus(orderId, new OrderStatusRequest(order.getOrderStatus()));

        // then
        assertEquals(order.getOrderStatus(), savedOrder.getOrderStatus());
    }

    @DisplayName("주문 상태가 계산완료 상태면 주문 상태를 갱신할 수 없다.")
    @Test
    void changeOrderStatusIsCompletion() {
        // given
        Long orderId = 1L;
        OrderLineItem orderLineItem = 주문_항목_생성(1L, 1);
        OrderTable orderTable = 주문_테이블_생성(1L, 0, false);
        Order order = 주문_생성(orderId, orderTable.getId(),
            OrderStatus.COMPLETION.name(), Collections.singletonList(orderLineItem));
        OrderStatusRequest orderStatusRequest = new OrderStatusRequest(order.getOrderStatus());

        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));

        // when && then
        assertThrows(IllegalArgumentException.class, () ->
            orderService.changeOrderStatus(orderId, orderStatusRequest));
        verify(orderRepository, times(0)).save(order);
    }

    private Order 주문_생성(Long orderId, Long orderTableId, List<OrderLineItem> orderLineItems) {
        return new Order(orderId, orderTableId, orderLineItems);
    }

    private Order 주문_생성(Long orderId, Long orderTableId, String orderStatus, List<OrderLineItem> orderLineItems) {
        return new Order(orderId, orderTableId, orderStatus, orderLineItems);
    }

    private OrderLineItem 주문_항목_생성(Long menuId, long quantity) {
        return new OrderLineItem(menuId, quantity);
    }
}
