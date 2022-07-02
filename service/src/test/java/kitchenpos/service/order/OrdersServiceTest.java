package kitchenpos.service.order;

import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.Orders;
import kitchenpos.domain.order.OrdersRepository;
import kitchenpos.service.order.application.OrderService;
import kitchenpos.service.order.application.OrdersValidator;
import kitchenpos.service.order.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrdersServiceTest {
    @Mock
    private OrdersRepository ordersRepository;
    @Mock
    private OrdersValidator ordersValidator;
    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("주문을 생성할 수 있다.")
    void create() {
        //when
        OrderResponse response =
                orderService.create(new OrdersRequest(0L, Arrays.asList(new OrderLineItemRequest(0L, 1))));

        //then
        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(response.getOrderLineItems().stream().map(OrderLineItemResponse::getQuantity)
                .collect(Collectors.toList())).containsExactlyInAnyOrder(1L);
    }

    @Test
    @DisplayName("전체 주문을 조회할 수 있다.")
    void list() {
        //given
        given(ordersRepository.findAll()).willReturn(
                Arrays.asList(new Orders(0L, OrderStatus.COOKING),
                        new Orders(1L, OrderStatus.COMPLETION)));

        //then
        assertThat(orderService.list().stream().map(OrderResponse::getOrderStatus)
                .collect(Collectors.toList())).containsExactlyInAnyOrder(OrderStatus.COMPLETION, OrderStatus.COOKING);
    }

    @Test
    @DisplayName("주문 상태를 변경할 수 있다.")
    void changeOrderStatus() {
        //given
        Orders orders = new Orders(0L, OrderStatus.COOKING);
        given(ordersRepository.findById(any())).willReturn(Optional.of(orders));

        //when
        OrderResponse savedOrder = orderService.changeOrderStatus(0L, new OrderStatusRequest(OrderStatus.COMPLETION));

        //then
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @Test
    @DisplayName("조회되지 않는 주문은 상태를 변경할 수 없다.")
    void changeOrderStatus_failed_1() {
        //given
        given(ordersRepository.findById(any())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> orderService.changeOrderStatus(0L,
                new OrderStatusRequest(OrderStatus.COMPLETION))).isExactlyInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("계산 완료된 주문은 상태를 변경할 수 없다.")
    void changeOrderStatus_failed_2() {
        //given
        given(ordersRepository.findById(any())).willReturn(
                Optional.of(new Orders(0L, OrderStatus.COMPLETION)));

        //then
        assertThatThrownBy(() -> orderService.changeOrderStatus(0L,
                new OrderStatusRequest(OrderStatus.COMPLETION))).isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
