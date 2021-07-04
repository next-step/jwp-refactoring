package kitchenpos.application;

import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderRequest;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    OrderService orderService;

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
                Lists.list(new OrderLineItemRequest(1L, 10L),
                        new OrderLineItemRequest(2L, 20L)));

        // Order 를 만들기 위한 Dummy Data
        OrderLineItem orderLineItem1 = OrderLineItem.of(order, MENU_ID_1L, 10);
        ReflectionTestUtils.setField(orderLineItem1, "seq", ORDER_LINE_ITEM_ID_1L);
        OrderLineItem orderLineItem2 = OrderLineItem.of(order, MENU_ID_2L, 20);
        ReflectionTestUtils.setField(orderLineItem2, "seq", ORDER_LINE_ITEM_ID_2L);

        orderTable = OrderTable.of(10, false);

        order = Order.of(orderTable, OrderStatus.COOKING, Lists.list(orderLineItem1, orderLineItem2));
    }

    @Test
    @DisplayName("주문 항목이 비어있다면 주문을 등록할 수 없다.")
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

        given(menuRepository.countByIdIn(Lists.list(ORDER_LINE_ITEM_ID_1L)))
                .willReturn(100L);

        OrderLineItem orderLineItem1 = OrderLineItem.of(order, MENU_ID_1L, 10);
        ReflectionTestUtils.setField(orderLineItem1, "seq", ORDER_LINE_ITEM_ID_1L);

        orderRequest = new OrderRequest(ANY_ORDER_TABLE_ID, Lists.list(new OrderLineItemRequest(MENU_ID_1L, 10L)));

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Not Same as orderLineItems");
    }

    @Test
    @DisplayName("주문한 테이블이 빈 테이블일 경우 주문을 등록할 수 없다.")
    void exception3_create() {
        orderTable.changeEmptyTable();

        given(orderTableRepository.findById(ANY_ORDER_TABLE_ID)).willReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Should have not orderTable empty");
    }

    @Test
    @DisplayName("처음 주문 상태(order status)는 조리(COOKING) 상태가 된다.")
    void after_create_orderStatus_is_COOKING() {
        orderTable.changeNonEmptyTable();
        given(orderTableRepository.findById(ANY_ORDER_TABLE_ID)).willReturn(Optional.of(orderTable));
        given(menuRepository.countByIdIn(Lists.list(MENU_ID_1L, MENU_ID_2L))).willReturn(2L);
        given(orderRepository.save(any())).willReturn(order);

        Order saveOrder = orderService.create(orderRequest);

        assertThat(saveOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @Test
    @DisplayName("주문의 주문 상태(order status)를 식사 상태로 변경할 수 있다.")
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