package kitchenpos.tablegroup.domain;

import kitchenpos.ordertable.exception.OrderTableService;
import kitchenpos.ordertable.domain.OrderTable;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;

@DisplayName("단체 지정 생성시 주문테이블 유효성 테스트")
@ExtendWith(MockitoExtension.class)
class OrderTableIdsTableGroupValidatorTest {
    @Mock
    private OrderTableService orderTableService;
    @InjectMocks
    private OrderTableIdsTableGroupValidator validator;


    @DisplayName("모두 빈 상태가 아닐때 유효하지 못하다.")
    @Test
    void validateFail() {
        // given
        final List<Long> orderTableIds = Arrays.asList(1L, 2L);
        final List<OrderTable> expected = Arrays.asList(
                OrderTable.of(3, false), OrderTable.of(3, true)
        );
        given(orderTableService.getOrderTablesByIdIn(orderTableIds)).willReturn(expected);
        // when
        final ThrowableAssert.ThrowingCallable throwingCallable = () -> validator.validate(orderTableIds);
        // then
        assertThatIllegalArgumentException().isThrownBy(throwingCallable);
    }

    @DisplayName("모두 빈 상태 일떄 유효하지 못하다.")
    @Test
    void validateSuccess() {
        // given
        final List<Long> orderTableIds = Arrays.asList(1L, 2L);
        final List<OrderTable> expected = Arrays.asList(
                OrderTable.of(3, true), OrderTable.of(3, true)
        );
        given(orderTableService.getOrderTablesByIdIn(orderTableIds)).willReturn(expected);
        // when
        final Executable executable = () -> validator.validate(orderTableIds);
        // then
        assertDoesNotThrow(executable);
    }
}