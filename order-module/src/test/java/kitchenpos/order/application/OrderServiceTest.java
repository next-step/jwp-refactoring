package kitchenpos.order.application;

import kitchenpos.exception.OrderException;
import kitchenpos.order.enums.OrderStatus;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.publisher.OrderEventPublisher;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("메뉴 관련 기능 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderEventPublisher eventPublisher;

    @InjectMocks
    private OrderService orderService;

    private OrderRequest 주문_요청;
    private OrderTable 주문_테이블;
    private Orders 주문;
    private List<OrderLineItem> 주문_항목;
    private List<OrderLineItemRequest> orderLineItemRequests;

    @BeforeEach
    void setUp() {
        orderLineItemRequests = Arrays.asList(
                new OrderLineItemRequest(1L, 1),
                new OrderLineItemRequest(2L, 1)
        );
        주문_요청 = new OrderRequest(1L, null, null, orderLineItemRequests);
        주문_테이블 = new OrderTable(1L, 4, false);
        주문 = new Orders(1L, 1L, OrderStatus.COOKING, LocalDateTime.now());
        주문_항목 = Arrays.asList(
                new OrderLineItem(1L, 주문, 1L, 1),
                new OrderLineItem(2L, 주문, 2L, 1)
        );
    }

    @Test
    void 주문_생성_기능() {
        주문.addOrderLineItems(주문_항목);
        when(orderRepository.save(any(Orders.class))).thenReturn(주문);

        OrderResponse expected = orderService.create(주문_요청);

        assertThat(expected.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(expected.getOrderTableId()).isEqualTo(주문_테이블.getId());
        assertThat(expected.getOrderLineItemResponses().size()).isEqualTo(2);
        Set<Long> orderIds = expected.getOrderLineItemResponses().stream().map(OrderLineItemResponse::getOrderId).collect(Collectors.toSet());
        assertThat(orderIds.size()).isEqualTo(1);
        assertThat(orderIds).contains(expected.getId());
    }

    @Test
    void 주문_아이템이_존재하지않을경우_에러발생() {
        주문_요청 = new OrderRequest(1L, null, null, Collections.emptyList());
        assertThatThrownBy(() -> orderService.create(주문_요청)).isInstanceOf(OrderException.class);

        주문_요청 = new OrderRequest(1L, null, null, null);
        assertThatThrownBy(() -> orderService.create(주문_요청)).isInstanceOf(OrderException.class);
    }

    @Test
    void 주문_리스트_조회() {
        주문.addOrderLineItems(주문_항목);
        when(orderRepository.findAll()).thenReturn(Arrays.asList(주문));

        List<OrderResponse> orders = orderService.list();
        assertThat(orders.size()).isEqualTo(1);
        OrderResponse expected = orders.get(0);

        assertThat(expected.getOrderTableId()).isEqualTo(주문_테이블.getId());
        assertThat(expected.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(expected.getOrderLineItemResponses().size()).isEqualTo(2);
    }

    @Test
    void 주문_상태_변경_식사() {
        주문_요청 = new OrderRequest(9L, OrderStatus.MEAL, null, orderLineItemRequests);
        주문.addOrderLineItems(주문_항목);
        when(orderRepository.findById(1L)).thenReturn(Optional.ofNullable(주문));
        OrderResponse expected = orderService.changeOrderStatus(주문.getId(), 주문_요청);
        assertThat(expected.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
        assertThat(expected.getOrderLineItemResponses().size()).isEqualTo(2);
    }

    @Test
    void 존재하지않는_주문의_상태_변경_시_에러발생() {
        주문_요청 = new OrderRequest(9L, OrderStatus.MEAL, null, orderLineItemRequests);
        when(orderRepository.findById(주문.getId())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), 주문_요청)).isInstanceOf(OrderException.class);
    }
}
