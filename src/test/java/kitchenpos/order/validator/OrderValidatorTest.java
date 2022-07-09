package kitchenpos.order.validator;

import kitchenpos.order.dao.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static kitchenpos.table.TableGenerator.주문_테이블_생성;
import static kitchenpos.table.domain.NumberOfGuestsTest.손님_수_생성;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderValidator orderValidator;

    private OrderTable 주문_테이블 = spy(주문_테이블_생성(손님_수_생성(10)));

    @DisplayName("주문 테이블의 주문이 종료 상태가 아닌 상태인 주문 테이블의 빈 자리 여부 변경여부를 체크하면 예외가 발생해야 한다")
    @Test
    void changeEmptyByNotCompletionOrderStatusTest() {
        // given
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), any())).thenReturn(true);

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> orderValidator.isPossibleChangeEmpty(주문_테이블));
    }

    @DisplayName("단체 지정에 포함되지 않고 완료 상태의 주문 테이블의 빈 자리 여부를 체크하면 예외가 발생하지 않아야 한다")
    @Test
    void changeEmptyTest() {
        // given
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), any())).thenReturn(false);

        // then
        assertThatNoException().isThrownBy(() -> orderValidator.isPossibleChangeEmpty(주문_테이블));
    }
}