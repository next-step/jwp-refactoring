package kitchenpos.order.service;


import kitchenpos.common.domain.Quantity;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@DisplayName("주문 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderValidator orderValidator;

    @Mock
    private OrderRepository orderRepository;

    @DisplayName("주문을 생성한다.")
    @Test
    void 주문_생성() {
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 1L);
        final OrderRequest orderRequest = new OrderRequest(1L, null, Collections.singletonList(orderLineItemRequest));
        final Order order = orderRequest.toOrder();

        given(orderRepository.save(order)).willReturn(order);

        OrderResponse response = orderService.create(orderRequest);

        assertAll(
                () -> assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
                () -> assertThat(response.getOrderLineItems().size()).isEqualTo(1)
        );
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void 주문_목록_조회() {
        // given
        Order firstOrder = Order.of(1L, Collections.singletonList(OrderLineItem.of(1L, Quantity.of(2L))));
        Order secondOrder = Order.of(1L, Collections.singletonList(OrderLineItem.of(2L, Quantity.of(1L))));
        given(orderRepository.findAll()).willReturn(Arrays.asList(firstOrder, secondOrder));

        // when
        List<OrderResponse> response = orderService.list();

        // then
        assertThat(response.size()).isEqualTo(2);
    }
}
