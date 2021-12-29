package kitchenpos.orders.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.common.domain.Validator;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.orders.order.domain.Order;
import kitchenpos.orders.order.domain.OrderLineItems;
import kitchenpos.orders.order.domain.OrderRepository;
import kitchenpos.orders.order.domain.OrderStatus;
import kitchenpos.orders.order.dto.OrderChangeStatusRequest;
import kitchenpos.orders.order.dto.OrderLineItemRequest;
import kitchenpos.orders.order.dto.OrderRequest;
import kitchenpos.orders.order.dto.OrderResponse;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private Validator<Order> orderValidator;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("주문을 요청할 수 있다.")
    @Test
    void place() {
        // given
        final Long expectedOrderTableId = 1L;
        final OrderLineItems expectedItems = OrderLineItemFixture.ofList(
            OrderLineItemFixture.of(1L, 1L),
            OrderLineItemFixture.of(2L, 1L)
        );
        final Order expectedOrder = OrderFixture.of(
            1L,
            expectedOrderTableId,
            expectedItems,
            orderValidator
        );

        given(orderRepository.save(any(Order.class))).willReturn(expectedOrder);

        final Long orderTableId = 1L;
        final List<OrderLineItemRequest> items = Arrays.asList(
            OrderLineItemFixture.ofRequest(1L, 1L),
            OrderLineItemFixture.ofRequest(2L, 1L)
        );
        final OrderRequest request = OrderFixture.ofRequest(orderTableId, items);

        // when
        final OrderResponse response = orderService.place(request);

        // then
        assertAll(
            () -> assertThat(response.getId()).isNotNull(),
            () -> assertThat(response.getStatus()).isEqualTo("COOKING"),
            () -> assertThat(response.getOrderLineItems().size()).isEqualTo(2)
        );
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        final Order order1 = OrderFixture.of(
            1L,
            1L,
            OrderLineItemFixture.ofList(
                OrderLineItemFixture.of(1L, 1L),
                OrderLineItemFixture.of(2L, 1L)
            ),
            orderValidator
        );
        final Order order2 = OrderFixture.of(
            2L,
            2L,
            OrderLineItemFixture.ofList(
                OrderLineItemFixture.of(3L, 1L),
                OrderLineItemFixture.of(4L, 1L)
            ),
            orderValidator
        );
        final List<Order> expectedOrders = Arrays.asList(order1, order2);

        given(orderRepository.findAll()).willReturn(expectedOrders);

        // when
        final List<OrderResponse> response = orderService.list();

        // then
        final List<Long> expectedOrderIds = expectedOrders.stream()
            .map(Order::getId)
            .collect(Collectors.toList());
        final List<Long> actualOrderIds = response.stream()
            .map(OrderResponse::getId)
            .collect(Collectors.toList());
        assertAll(
            () -> assertThat(response.size()).isEqualTo(2),
            () -> assertThat(actualOrderIds).containsExactlyElementsOf(expectedOrderIds)
        );
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        // given
        final Order expected = OrderFixture.of(
            1L,
            1L,
            OrderLineItemFixture.ofList(
                OrderLineItemFixture.of(1L, 1L),
                OrderLineItemFixture.of(2L, 1L)
            ),
            orderValidator
        );

        given(orderRepository.findById(anyLong())).willReturn(Optional.of(expected));

        final OrderChangeStatusRequest request = OrderFixture.ofChangeStatusRequest(
            OrderStatus.MEAL
        );

        // when
        final OrderResponse response = orderService.changeStatus(1L, request);

        // then
        assertThat(response.getStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("요청 되지 않은 주문의 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatusFailNoSuchOrder() {
        // given
        final OrderChangeStatusRequest request = OrderFixture.ofChangeStatusRequest(
            OrderStatus.MEAL
        );

        // when
        final ThrowableAssert.ThrowingCallable response = () ->
            orderService.changeStatus(1L, request);

        // then
        assertThatThrownBy(response).isInstanceOf(NoSuchElementException.class);
    }


    @DisplayName("이미 완료된 주문의 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatusFailOrderCompleted() {
        // given
        final Order expected = OrderFixture.of(
            1L,
            1L,
            OrderLineItemFixture.ofList(
                OrderLineItemFixture.of(1L, 1L),
                OrderLineItemFixture.of(2L, 1L)
            ),
            orderValidator
        );
        expected.changeStatus(OrderStatus.COMPLETION);

        given(orderRepository.findById(anyLong())).willReturn(Optional.of(expected));

        final OrderChangeStatusRequest request = OrderFixture.ofChangeStatusRequest(
            OrderStatus.MEAL
        );

        // when
        final ThrowableAssert.ThrowingCallable response = () ->
            orderService.changeStatus(1L, request);

        // then
        assertThatThrownBy(response).isInstanceOf(IllegalStateException.class);
    }
}
