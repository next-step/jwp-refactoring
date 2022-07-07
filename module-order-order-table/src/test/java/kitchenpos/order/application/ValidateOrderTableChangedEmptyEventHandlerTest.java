package kitchenpos.order.application;

import static kitchenpos.order.domain.OrderStatus.getCannotUngroupTableGroupStatus;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import kitchenpos.exception.UnCompletedOrderStatusException;
import kitchenpos.order.application.ValidateOrderTableChangedEmptyEventHandler;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTableChangedEmptyEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ValidateOrderTableChangedEmptyEventHandlerTest {
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private ValidateOrderTableChangedEmptyEventHandler validateOrderTableChangedEmptyEventHandler;

    @Test
    void 주문상태_조리_혹은_식사_예외() {
        //given
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(1L,
                getCannotUngroupTableGroupStatus())).willThrow(
                UnCompletedOrderStatusException.class);

        // when, then
        assertThatThrownBy(
                () -> validateOrderTableChangedEmptyEventHandler.handle(new OrderTableChangedEmptyEvent(1L))
        ).isInstanceOf(UnCompletedOrderStatusException.class);

    }
}
