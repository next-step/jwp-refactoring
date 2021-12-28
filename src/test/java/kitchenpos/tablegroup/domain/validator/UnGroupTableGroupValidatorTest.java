package kitchenpos.tablegroup.domain.validator;

import kitchenpos.order.infra.OrderRepository;
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
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@DisplayName("단체 지정 해산시 주문테이블 유효성 테스트")
@ExtendWith(MockitoExtension.class)
class UnGroupTableGroupValidatorTest {
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private UnGroupTableGroupValidator validator;

    @DisplayName("조리나 식사 상태일 경우일 경우 유효하다.")
    @Test
    void validateFail() {
        // given
        List<Long> orderTableIds = Arrays.asList(1L, 2L);
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);
        // when
        final ThrowableAssert.ThrowingCallable throwingCallable = () -> validator.validate(orderTableIds);
        // then
        assertThatIllegalArgumentException().isThrownBy(throwingCallable);
    }

    @DisplayName("조리나 식사 상태일 경우가 아닐 경우에만 유효하다.")
    @Test
    void validate() {
        // given
        List<Long> orderTableIds = Arrays.asList(1L, 2L);
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(false);
        // when
        final Executable executable = () -> validator.validate(orderTableIds);
        // then
        assertDoesNotThrow(executable);
    }
}