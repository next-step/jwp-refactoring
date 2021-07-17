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

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;

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

    private final Long orderId = 1L;
    private Order order;
    private Order savedOrder;
    private OrderLineItem orderLineItem;
    private OrderLineItem orderLineItem2;

    @BeforeEach
    void setUp() {
        order = new Order();
        savedOrder = new Order();
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
        final Menu menu1 = mock(Menu.class);
        final Menu menu2 = mock(Menu.class);
        given(menu1.getId()).willReturn(1L);
        given(menu2.getId()).willReturn(2L);
        given(menuRepository.findAllByIdIn(anyList())).willReturn(Arrays.asList(menu1, menu2));
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(new OrderTable()));

        savedOrder.setId(1L);
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
        final OrderRequest order = new OrderRequest();
        order.setOrderStatus("COOKING");
        final Order savedOrder = new Order();
        given(orderRepository.findById(orderId)).willReturn(Optional.of(savedOrder));

        // when
        orderService.changeOrderStatus(orderId, order);

        // then
        verify(orderRepository).save(savedOrder);
        assertThat(savedOrder.getOrderStatus().name()).isEqualTo(order.getOrderStatus());
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
