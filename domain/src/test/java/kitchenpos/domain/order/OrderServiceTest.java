package kitchenpos.domain.order;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.event.order.OrderCreatedEvent;
import kitchenpos.exception.order.InvalidOrderLineItemsException;
import kitchenpos.repository.order.OrderRepository;
import kitchenpos.repository.order.OrderTableRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private OrderService orderService;

    private Order order;

    private OrderTable orderTable;
    private final static long ANY_ORDER_ID = 1L;
    private final static long ANY_ORDER_TABLE_ID = 1L;

    private final static long ORDER_LINE_ITEM_ID_1L = 1L;
    private final static long ORDER_LINE_ITEM_ID_2L = 2L;
    private final static long MENU_ID_1L = 1L;
    private final static long MENU_ID_2L = 2L;
    private OrderRequest orderRequest;

    @BeforeEach
    void setUp() {
        orderRequest = new OrderRequest(ANY_ORDER_TABLE_ID,
                Lists.list(new OrderLineItemRequest(MENU_ID_1L, 10L), new OrderLineItemRequest(MENU_ID_2L, 20L)));

        // Order 를 만들기 위한 Dummy Data
        OrderLineItem orderLineItem1 = OrderLineItem.of(order, MENU_ID_1L, 10);
        ReflectionTestUtils.setField(orderLineItem1, "seq", ORDER_LINE_ITEM_ID_1L);
        OrderLineItem orderLineItem2 = OrderLineItem.of(order, MENU_ID_2L, 20);
        ReflectionTestUtils.setField(orderLineItem2, "seq", ORDER_LINE_ITEM_ID_2L);

        orderTable = OrderTable.of(10, false);

        order = Order.of(orderTable, OrderStatus.COOKING, Lists.list(orderLineItem1, orderLineItem2));
    }

    @Test
    @DisplayName("주문 항목(OrderLineItem)이 없다면 주문을 등록할 수 없다.")
    void exception_create() {
        given(orderTableRepository.findById(ANY_ORDER_ID)).willReturn(Optional.of(orderTable));
        orderRequest = new OrderRequest(ANY_ORDER_TABLE_ID, new ArrayList<>());

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("should have orderLineItems");
    }

    @Test
    @DisplayName("주문 항목의 갯수가 주문 항목의 메뉴의 갯수와 일치 하지 않으면 등록할 수 없다.")
    void exception2_create() {
        given(orderTableRepository.findById(ANY_ORDER_TABLE_ID)).willReturn(Optional.of(orderTable));
        doThrow(InvalidOrderLineItemsException.class)
                .when(eventPublisher).publishEvent(any(OrderCreatedEvent.class));

        orderRequest = new OrderRequest(ANY_ORDER_TABLE_ID, Lists.list(new OrderLineItemRequest(MENU_ID_1L, 10L)));

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(InvalidOrderLineItemsException.class);
    }

    @Test
    @DisplayName("주문된 테이블이 빈 테이블일 경우 주문이 수행되지 않는다.")
    void exception3_create() {
        orderTable.changeEmptyTable();

        given(orderTableRepository.findById(ANY_ORDER_TABLE_ID)).willReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Should have not orderTable empty");
    }

    @Test
    @DisplayName("주문이 들어가면 처음 상태(order status)는 조리(COOKING) 상태가 된다.")
    void after_create_orderStatus_is_COOKING() {
        orderTable.changeNonEmptyTable();
        given(orderTableRepository.findById(ANY_ORDER_TABLE_ID)).willReturn(Optional.of(orderTable));
        given(orderRepository.save(any())).willReturn(order);

        orderRequest = new OrderRequest(ANY_ORDER_TABLE_ID,
                Lists.list(new OrderLineItemRequest(MENU_ID_1L, 10L), new OrderLineItemRequest(MENU_ID_2L, 10L)));

        Order saveOrder = orderService.create(orderRequest);

        assertThat(saveOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @Test
    @DisplayName("주문의 상태(order status)를 식사 상태로 변경할 수 있다.")
    void changeOrderStatusTest() {
        given(orderRepository.findById(ANY_ORDER_ID)).willReturn(Optional.of(order));

        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.COOKING);

        Order changedOrder = orderService.changeOrderStatus(ANY_ORDER_ID, OrderStatus.MEAL);
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @Test
    @DisplayName("이미 계산이 완료된 주문은 주문 상태(order status)를 바꿀 수 없다.")
    void exception_changeOrderStatusTest() {
        given(orderRepository.findById(ANY_ORDER_ID)).willReturn(Optional.of(order));

        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.COMPLETION);

        assertThatThrownBy(() -> orderService.changeOrderStatus(ANY_ORDER_ID, OrderStatus.COMPLETION))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid OrderStatus");
    }
}