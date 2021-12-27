package kitchenpos.table.domain;

import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableGroupValidatorTest {
    @Mock
    private OrderStatusChecker orderStatusChecker;
    @InjectMocks
    private TableGroupValidator tableGroupValidator;

    @DisplayName("단체 지정이 되지 않은 빈 테이블만 단체 지정 가능. 아니면 예외 발생")
    @ParameterizedTest
    @MethodSource("provideInvalidTable")
    void validateUngroupedEmptyTable(final OrderTable orderTable) {
        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> tableGroupValidator.validateToGroup(singletonList(orderTable)))
            .withMessageContaining("아직 단체 지정이 되지 않은 빈 테이블만 단체 지정이 가능합니다");
    }

    private static Stream<OrderTable> provideInvalidTable() {
        return Stream.of(new OrderTable(1L, 2, true), new OrderTable(2, false));
    }

    @DisplayName("모든 주문이 계산 완료 상태일 때만 단체 지정 해제 가능. 아니면 예외 발생")
    @Test
    void validateCompletedOrder() {
        // given
        final TableGroup tableGroup = new TableGroup(singletonList(new OrderTable(1L, 2, false)));
        when(orderStatusChecker.existsNotCompletedOrderByOrderTableIds(any()))
            .thenReturn(true);

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> tableGroupValidator.validateToUngroup(tableGroup))
            .withMessageContaining("주문 상태가 '조리' 혹은 '식사'일 경우, 단체 지정을 해제할 수 없습니다");
    }
}
