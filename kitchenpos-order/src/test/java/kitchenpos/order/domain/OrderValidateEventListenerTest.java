package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import kitchenpos.common.event.OrderTableChangeOrderCloseEvent;
import kitchenpos.order.exception.OrderIsNotCompleteException;
import java.util.Arrays;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderValidateEventListenerTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderValidateEventListener orderValidateEventListener;

    @DisplayName("주문상태가 계산완료가 아닌 주문이 있는 경우 예외")
    @Test
    void validateAllOrdersInTableComplete() {
        //given
        List<Order> orders = Arrays.asList(
            new Order(1L, 1L, OrderStatus.COMPLETION, Lists.emptyList()),
            new Order(2L, 1L, OrderStatus.COMPLETION, Lists.emptyList()),
            new Order(3L, 1L, OrderStatus.COOKING, Lists.emptyList()));
        특정_테이블의_전체주문_조회_모킹(orders);

        OrderTableChangeOrderCloseEvent orderValidateEvent = new OrderTableChangeOrderCloseEvent(
            this, 1L);
        //when, then
        assertThatThrownBy(
            () -> orderValidateEventListener.validateAllOrdersInTableComplete(orderValidateEvent))
            .isInstanceOf(OrderIsNotCompleteException.class);
    }

    private void 특정_테이블의_전체주문_조회_모킹(List<Order> expectedOrders) {
        given(orderRepository.findAllByOrderTableId(any()))
            .willReturn(expectedOrders);
    }
}
