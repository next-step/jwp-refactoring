package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderTableEmptyValidatorTest {
    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderTableEmptyValidator orderTableEmptyValidator;

    @DisplayName("테이블이 존재하지 않을 경우 예외가 발생한다.")
    @Test
    void notExistException() {
        given(orderTableRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderTableEmptyValidator.validateTableEmpty(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 테이블입니다.");
    }

    @DisplayName("테이블이 비어있을 경우 예외가 발생한다.")
    @Test
    void notEmptyException() {
        OrderTable expected = new OrderTable(0, true);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(expected));

        assertThatThrownBy(() -> orderTableEmptyValidator.validateTableEmpty(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이용중이지 않은 테이블에서는 주문 할 수 없습니다.");
    }
}
