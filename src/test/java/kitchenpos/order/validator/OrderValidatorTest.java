package kitchenpos.order.validator;

import kitchenpos.order.dao.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tableGroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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

    @DisplayName("요리중 또는 식사중 상태인 주문 테이블이 포함된 단체 지정을 해제 요청시 예외가 발생해야 한다")
    @Test
    void validateUngroupByNotValidOrderStatusTest() {
        // given
        when(주문_테이블.getId()).thenReturn(0L);
        when(주문_테이블.isEmpty()).thenReturn(true);
        when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(true);
        TableGroup 테이블_그룹 = 테이블_그룹_생성(주문_테이블_목록_생성(Arrays.asList(주문_테이블, 주문_테이블)));

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> orderValidator.possibleUngroupTableGroup(테이블_그룹));
    }

    @DisplayName("요리중 또는 식사중 상태인 주문 테이블이 포함되어 있지 않은 단체 지정을 검사하면 예외가 발생하지 않아야 한다")
    @Test
    void ungroupTest() {
        // given
        when(주문_테이블.getId()).thenReturn(0L);
        when(주문_테이블.isEmpty()).thenReturn(false);
        when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(false);
        OrderTables 주문_테이블_목록 = 주문_테이블_목록_생성(Arrays.asList(주문_테이블, 주문_테이블));

        TableGroup 테이블_그룹 = 테이블_그룹_생성(주문_테이블_목록);

        // then
        assertThatNoException().isThrownBy(() -> orderValidator.possibleUngroupTableGroup(테이블_그룹));
    }
}