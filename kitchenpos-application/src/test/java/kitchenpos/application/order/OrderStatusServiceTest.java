package kitchenpos.application.order;

import kitchenpos.application.order.dto.OrderRequest;
import kitchenpos.application.order.dto.OrderResponse;
import kitchenpos.core.domain.Order;
import kitchenpos.core.domain.OrderRepository;
import kitchenpos.core.domain.OrderStatus;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static kitchenpos.application.order.OrderServiceFixture.getChangeRequest;
import static kitchenpos.core.OrderFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@DisplayName("주문 상태 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class OrderStatusServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderStatusService orderStatusService;

    @DisplayName("주문 아이디를 통해 주문 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        // given
        OrderRequest changeRequest = getChangeRequest(OrderStatus.COMPLETION.name());
        final Order expected = getOrder(1L, 1L, OrderStatus.MEAL, getOrderLineItems(
                getOrderLineItem(1L, 3), getOrderLineItem(2L, 4))
        );

        given(orderRepository.findById(anyLong())).willReturn(Optional.of(expected));
        // when
        final OrderResponse actual = orderStatusService.changeOrderStatus(1L, changeRequest);
        // then
        assertThat(actual.getOrderStatus()).isEqualTo(expected.getOrderStatus());
    }


    @DisplayName("주문 상태를 변경할 수 없는 경우")
    @Nested
    class ChangeOrderStatusFail {
        @DisplayName("주문 아이디를 따른 주문이 존재하지 않는 경우")
        @Test
        void changeOrderStatusByEmptyOrder() {
            // given
            OrderRequest changeRequest = getChangeRequest(OrderStatus.COOKING.name());
            given(orderRepository.findById(anyLong())).willReturn(Optional.empty());
            // when
            ThrowableAssert.ThrowingCallable callable = () -> orderStatusService.changeOrderStatus(1L, changeRequest);
            // then
            assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 상태가 계산완료인 경우")
        @Test
        void changeOrderStatusByCompletionStatus() {
            // given
            final OrderRequest changeRequest = getChangeRequest(OrderStatus.COOKING.name());
            final Order expected = getOrder(1L, 1L, OrderStatus.COMPLETION, getOrderLineItems(
                    getOrderLineItem(1L, 3), getOrderLineItem(2L, 4))
            );
            given(orderRepository.findById(anyLong())).willReturn(Optional.of(expected));

            // when
            ThrowableAssert.ThrowingCallable callable = () -> orderStatusService.changeOrderStatus(1L, changeRequest);
            // then
            assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
        }
    }

}
