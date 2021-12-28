package kitchenpos.ordertable.domain;

import kitchenpos.order.infra.OrderRepository;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static kitchenpos.ordertable.application.OrderTableServiceTest.getOrderTable;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@DisplayName("주문 테이블 변경시  테스트")
@ExtendWith(MockitoExtension.class)
class ChangeEmptyOrderOrderTableValidatorTest {
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private ChangeEmptyOrderOrderTableValidator validator;

    @DisplayName("주문 테이블의 주문 상태가 조리나 식사일 경우 유효하지 못하다.")
    @Test
    void validateFail() {
        // given
        final OrderTable orderTable = getOrderTable(1L, true, 4);
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(false);
        // when
        final ThrowableAssert.ThrowingCallable throwingCallable = () -> validator.validate(orderTable.getId());
        // then
        assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 주문 상태가 조리나 식사일 경우가 아닐경우 유효하다.")
    @Test
    void validate() {
        // given
        final OrderTable orderTable = getOrderTable(1L, true, 4);
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(true);
        // when
        final Executable executable = () -> validator.validate(orderTable.getId());
        // then
        assertDoesNotThrow(executable);
    }
}