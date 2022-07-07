package kitchenpos.order.application;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ErrorCode;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private OrderService orderService;

    private final Long ORDER_ID = 1L;
    private final Long ORDER_TABLE_ID = 1L;

    private OrderLineItemRequest orderLineItemRequest;
    private OrderRequest orderRequest;
    private Order order;

    @BeforeEach
    void setUp() {
        orderLineItemRequest = new OrderLineItemRequest(1L, 1);
        orderRequest = new OrderRequest(ORDER_TABLE_ID, Arrays.asList(orderLineItemRequest));
        order = new Order(ORDER_ID, ORDER_TABLE_ID, OrderStatus.COOKING,
                Arrays.asList(OrderLineItemRequest.toEntity(orderLineItemRequest)));
    }

    @DisplayName("주문을 생성한다")
    @Test
    void create() {
        given(menuRepository.countByIdIn(any())).willReturn(1L);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(new OrderTable(1L)));
        given(orderRepository.save(any(Order.class))).willReturn(order);

        OrderResponse response = orderService.create(orderRequest);

        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @DisplayName("존재하는 menu가 아니라면 주문을 생성할 수 없다.")
    @Test
    void create_invalid_menuIds() {
        given(menuRepository.countByIdIn(any())).willReturn(1000L);

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.CONTAINS_NOT_EXIST_MENU.getMessage());
    }

    @DisplayName("orderLineItems 비어있다면 주문을 생성할 수 없다.")
    @Test
    void create_empty_orderLineItems() {
        orderRequest = new OrderRequest(ORDER_TABLE_ID, new ArrayList<>());

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.ORDER_LINE_ITEM_EMPTY.getMessage());
    }

    @DisplayName("존재하는 주문 테이블이 아니라면 주문을 생성할 수 없다.")
    @Test
    void create_invalid_orderTableId() {
        given(menuRepository.countByIdIn(any())).willReturn(1L);
        given(orderTableRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.ORDER_TABLE_NOT_FOUND.getMessage());
    }

    @DisplayName("주문 테이블이 비어있으면 주문을 생성할 수 없다.")
    @Test
    void create_orderTable_empty() {
        given(menuRepository.countByIdIn(any())).willReturn(1L);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(new OrderTable(1L, true)));

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.ORDER_TABLE_EMPTY.getMessage());
    }

    @DisplayName("주문 목록을 조회한다")
    @Test
    void list() {
        Order order = new Order(1L, Arrays.asList(new OrderLineItem(), new OrderLineItem()));
        given(orderRepository.findAllWithOrderLineItems()).willReturn(Arrays.asList(order));

        List<OrderResponse> responses = orderService.list();

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getOrderLineItems()).hasSize(2);
    }

    @DisplayName("주문 상태를 변경한다")
    @Test
    void changeOrderStatus() {
        given(orderRepository.findById(ORDER_ID)).willReturn(Optional.of(order));

        OrderResponse response = orderService.changeOrderStatus(ORDER_ID, new OrderRequest(OrderStatus.COMPLETION));

        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @DisplayName("존재하는 주문이 없다면 주문 상태를 변경할 수 없다")
    @Test
    void changeOrderStatus_invalid_orderId() {
        given(orderRepository.findById(ORDER_ID)).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.changeOrderStatus(ORDER_ID, new OrderRequest(OrderStatus.COMPLETION)))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.ORDER_NOT_FOUND.getMessage());
    }

    @DisplayName("존재하는 주문의 상태가 COMPLETION이라면, 주문 상태를 변경할 수 없다")
    @Test
    void changeOrderStatus_invalid_orderStatus() {
        order.changeOrderStatus(OrderStatus.COMPLETION);
        given(orderRepository.findById(ORDER_ID)).willReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.changeOrderStatus(ORDER_ID, new OrderRequest(OrderStatus.COMPLETION)))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.CAN_NOT_CHANGE_COMPLETED_ORDER_STATUS.getMessage());
    }
}
