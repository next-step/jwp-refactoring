package kitchenpos.table.validator;

import kitchenpos.order.dao.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static kitchenpos.table.TableGenerator.주문_테이블_목록_생성;
import static kitchenpos.table.TableGenerator.주문_테이블_생성;
import static kitchenpos.table.domain.NumberOfGuestsTest.손님_수_생성;
import static kitchenpos.tableGroup.TableGroupGenerator.테이블_그룹_생성;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableValidatorTest {

    @Mock
    private OrderRepository orderRepository;

    private TableValidator tableValidator;

    private OrderTable 주문_테이블 = spy(주문_테이블_생성(손님_수_생성(10)));

    @BeforeEach
    void setUp() {
        tableValidator = new TableValidator(orderRepository);
        when(주문_테이블.getId()).thenReturn(0L);
    }

    @DisplayName("단체 지정에 포함된 주문 테이블의 빈 자리 여부 변경여부를 체크하면 예외가 발생해야 한다")
    @Test
    void changeEmptyByBelongTableGroupTest() {
        // given
        주문_테이블.joinGroup(테이블_그룹_생성(주문_테이블_목록_생성(Arrays.asList(주문_테이블, 주문_테이블))));

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> tableValidator.isPossibleChangeEmpty(주문_테이블));
    }

    @DisplayName("주문 테이블의 주문이 종료 상태가 아닌 상태인 주문 테이블의 빈 자리 여부 변경여부를 체크하면 예외가 발생해야 한다")
    @Test
    void changeEmptyByNotCompletionOrderStatusTest() {
        // given
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), any())).thenReturn(true);

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> tableValidator.isPossibleChangeEmpty(주문_테이블));
    }

    @DisplayName("단체 지정에 포함되지 않고 완료 상태의 주문 테이블의 빈 자리 여부를 체크하면 예외가 발생하지 않아야 한다")
    @Test
    void changeEmptyTest() {
        // given
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), any())).thenReturn(false);

        // then
        assertThatNoException().isThrownBy(() -> tableValidator.isPossibleChangeEmpty(주문_테이블));
    }

    @DisplayName("주문 테이블이 빈 테이블 상태에서 손님 수 변경 여부를 체크하면 예외가 발생해야 한다")
    @Test
    void changeNumberOfGuestsByEmptyTest() {
        // given
        when(주문_테이블.isEmpty()).thenReturn(true);

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> tableValidator.isPossibleChangeNumberOfGuests(주문_테이블));
    }

    @DisplayName("주문 테이블이 빈 테이블이 아닐 때 손님 수 변경 여부를 체크하면 변경하면 예외가 발생하지 않아야 한다")
    @Test
    void changeNumberOfGuestsTest() {
        // given
        when(주문_테이블.isEmpty()).thenReturn(false);

        // then
        assertThatNoException().isThrownBy(() -> tableValidator.isPossibleChangeNumberOfGuests(주문_테이블));
    }
}
