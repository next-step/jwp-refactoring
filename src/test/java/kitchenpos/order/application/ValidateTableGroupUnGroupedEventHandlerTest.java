package kitchenpos.order.application;

import static kitchenpos.order.domain.OrderStatus.getCannotUngroupTableGroupStatus;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import kitchenpos.Exception.UnCompletedOrderStatusException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.TableGroupUnGroupedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ValidateTableGroupUnGroupedEventHandlerTest {
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private ValidateTableGroupUnGroupedEventHandler validateTableGroupUnGroupedEventHandler;

    @Test
    void 주문상태_조리_혹은_식사_예외() {
        // given
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(1L),
                getCannotUngroupTableGroupStatus())).willThrow(
                UnCompletedOrderStatusException.class);

        // when, then
        assertThatThrownBy(
                () -> validateTableGroupUnGroupedEventHandler.handle(new TableGroupUnGroupedEvent(Arrays.asList(1L)))
        ).isInstanceOf(UnCompletedOrderStatusException.class);
    }
}
