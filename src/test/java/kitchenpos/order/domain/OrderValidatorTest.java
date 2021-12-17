package kitchenpos.order.domain;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("주문 검증 테스트")
class OrderValidatorTest {

    @Test
    @DisplayName("비어있는 테이블로 검증을 하면 예외를 발생한다.")
    void validateThrowException() {
        // given
        OrderTable orderTable = new OrderTable(true);
        OrderValidator orderValidator = new OrderValidator(mock(OrderTableRepository.class));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> orderValidator.validate(orderTable));
    }
}
