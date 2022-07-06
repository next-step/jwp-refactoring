package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import kitchenpos.order.dao.OrderRepository;
import kitchenpos.table.dao.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {

    @InjectMocks
    OrderValidator orderValidator;

    @Mock
    OrderTableRepository orderTableRepository;

    @Mock
    OrderRepository orderRepository;

    @Test
    @DisplayName("주문테이블 정보가 존재하지 않으면 오류를 반환한다")
    void validateOrderTableCheck() {
        // given
        given(orderTableRepository.existsById(any())).willReturn(false);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> orderValidator.validateOrderTableCheck(1L)
        ).withMessageContaining("존재하지 않는 주문테이블 입니다.");
    }

    @Test
    @DisplayName("지정한 단체를 해제시 주문 상태가 계산 완료여야 한다")
    void validateOrderStatusCheck() {
        // given
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> orderValidator.validateOrderStatusCheck(Collections.singletonList(1L))
        ).withMessageContaining("계산 완료 상태가 아닌 경우 단체를 해제할 수 없습니다.");
    }

    @Test
    @DisplayName("현재 주문 상태가 계산 완료가 아닌 경우 테이블을 빈 상태로 변경 불가능하다")
    void validateChangeableOrderStatusCheck() {
        // given
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(true);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> orderValidator.validateChangeableOrderStatusCheck(1L)
        );
    }
}
