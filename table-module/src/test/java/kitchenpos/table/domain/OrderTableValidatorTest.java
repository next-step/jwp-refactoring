package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderTableValidatorTest {
    @Mock
    private OrderStatusChecker orderStatusChecker;
    @InjectMocks
    private OrderTableValidator orderTableValidator;

    @DisplayName("단체 지정이 되어있을 경우 예외 발생")
    @Test
    void validateUngrouped() {
        // given
        final OrderTable orderTable = new OrderTable(1L, 1, false);

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> orderTableValidator.validateToChangeEmpty(orderTable))
            .withMessageContaining("단체 지정이 된 주문 테이블의 상태는 변경할 수 없습니다");
    }

    @DisplayName("주문이 계산 완료 상태가 아니면 예외 발생")
    @Test
    void validateCompletedOrder() {
        // given
        final OrderTable orderTable = new OrderTable(1, false);
        when(orderStatusChecker.existsNotCompletedOrderByOrderTableIds(any()))
            .thenReturn(true);

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> orderTableValidator.validateToChangeEmpty(orderTable))
            .withMessageContaining("주문 상태가 '조리' 혹은 '식사' 중일 경우, 테이블의 상태를 변경할 수 없습니다");
    }
}
