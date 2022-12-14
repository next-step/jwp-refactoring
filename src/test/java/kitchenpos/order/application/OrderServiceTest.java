package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.OrderStatusChangeRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderRequest.OrderLineItemRequest;
import kitchenpos.order.dto.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderValidator orderValidator;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("주문을 등록할 수 있다.")
    @Test
    void create() {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 10);
        OrderRequest orderRequest = new OrderRequest(1L, Collections.singletonList(orderLineItemRequest));
        Order expected = new Order(1L);
        expected.addLineItem(new OrderLineItem(1L, 10));
        given(orderRepository.save(any(Order.class))).willReturn(expected);

        OrderResponse actual = orderService.create(orderRequest);

        verify(orderValidator).validateMenu(any(Order.class));
        verify(orderValidator).validateTable(any(Order.class));
        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @DisplayName("주문 항목이 비어있는 경우 주문을 등록할 수 없다.")
    @Test
    void createWithEmptyOrderLineItem() {
        OrderRequest orderRequest = new OrderRequest(1L, Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목은 비어있을 수 없습니다.");
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void list() {
        given(orderRepository.findAll()).willReturn(Arrays.asList(new Order(1L), new Order(2L)));

        List<OrderResponse> actual = orderService.list();

        assertThat(actual).hasSize(2);
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void changeStatus() {
        Order order = new Order(1L);
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        OrderResponse actual = orderService.changeOrderStatus(1L, new OrderStatusChangeRequest(OrderStatus.COOKING));

        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @DisplayName("등록되지 않은 주문 상태를 변경할 수 없다.")
    @Test
    void changeStatusNotExistOrder() {
        given(orderRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, new OrderStatusChangeRequest(OrderStatus.COOKING)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문이 존재하지 않습니다.");
    }

    @DisplayName("이미 완료된 주문은 상태를 변경할 수 없다.")
    @Test
    void changeStatusAlreadyCompletion() {
        Order order = new Order(1L);
        order.changeStatus(OrderStatus.COMPLETION);
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, new OrderStatusChangeRequest(OrderStatus.COOKING)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("완료된 주문의 상태를 변경할 수 없습니다.");
    }
}
