package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

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

    @Mock
    private OrderValidator orderValidator;

    @Mock
    private ApplicationEventPublisher publisher;

    @InjectMocks
    private OrderService orderService;

    private final Long orderId = 1L;
    private Order order;
    private Order savedOrder;
    private OrderLineItem orderLineItem;
    private OrderLineItem orderLineItem2;

    @BeforeEach
    void setUp() {
        order = new Order(1L, OrderStatus.COOKING);
        savedOrder = new Order(1L);
        orderLineItem = new OrderLineItem();
        orderLineItem2 = new OrderLineItem();
    }

    @Test
    void create() {
        // given
        final OrderLineItemRequest orderLineItemRequest1 = new OrderLineItemRequest(1L, 1);
        final OrderLineItemRequest orderLineItemRequest2 = new OrderLineItemRequest(2L, 1);
        final List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(orderLineItemRequest1,
            orderLineItemRequest2);
        final OrderRequest orderRequest = new OrderRequest(1L, orderLineItemRequests);
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(new OrderTable()));
        given(orderRepository.save(any(Order.class))).willReturn(savedOrder);

        // when
        orderService.create(orderRequest);

        // then
        verify(orderRepository).save(any(Order.class));
    }

    @DisplayName("아이템이 없는 주문을 생성할 때 예외가 발생하는지 테스트")
    @Test
    void given_OrderHasEmptyItem_when_Create_then_ThrownException() {
        // given
        final OrderRequest order = new OrderRequest();

        // when
        final Throwable throwable = catchThrowable(() -> orderService.create(order));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("아이템이 하나만 있는 주문을 생성할 때 예외가 발생하는지 테스트")
    @Test
    void given_OrderHasOnlyOneItem_when_Create_then_ThrownException() {
        // given
        final OrderRequest order = new OrderRequest(1L, Collections.singletonList(new OrderLineItemRequest()));

        // when
        final Throwable differentSizeException = catchThrowable(() -> orderService.create(order));

        // then
        assertThat(differentSizeException).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        // given
        given(orderRepository.findAll()).willReturn(Collections.singletonList(order));

        // when
        orderService.list();

        // then
        verify(orderRepository).findAll();
    }

    @Test
    void changeOrderStatus() {
        // given
        final List<OrderLineItemRequest> orderLineItems = Collections.singletonList(new OrderLineItemRequest(1L, 1L));
        final OrderRequest orderRequest = new OrderRequest(1L, orderLineItems, "COOKING");
        final Order savedOrder = new Order();
        given(orderRepository.findById(orderId)).willReturn(Optional.of(savedOrder));
        final Order newOrder = new Order(1L, OrderStatus.COOKING);
        given(orderRepository.save(savedOrder)).willReturn(newOrder);

        // when
        orderService.changeOrderStatus(orderId, orderRequest);

        // then
        verify(orderRepository).save(savedOrder);
        assertThat(savedOrder.getOrderStatus().name()).isEqualTo(orderRequest.getOrderStatus());
    }

    @Test
    void given_CompletedOrder_when_ChangeOrderStatus_then_ThrownException() {
        // given
        final OrderRequest order = new OrderRequest();
        order.setOrderStatus("COMPLETION");

        // when
        final Throwable throwable = catchThrowable(() -> orderService.changeOrderStatus(orderId, order));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }
}
