package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import kitchenpos.table.domain.Headcount;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 검증")
@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {

    @Mock
    OrderTableRepository tableRepository;

    @InjectMocks
    OrderValidator validator;

    @Test
    @DisplayName("검증")
    void validate() {
        //given
        when(tableRepository.table(anyLong()))
            .thenReturn(OrderTable.place(Headcount.from(5)));

        //when
        ThrowingCallable validateCallable = () ->
            validator.validate(mock(Order.class));

        //then
        assertThatNoException().isThrownBy(validateCallable);
    }

    @Test
    @DisplayName("주문 테이블은 채워져 있어야 함")
    void validate_emptyTable_thrownIllegalArgumentException() {
        //given
        when(tableRepository.table(anyLong()))
            .thenReturn(OrderTable.empty(Headcount.from(2)));

        //when
        ThrowingCallable validateCallable = () -> validator.validate(mock(Order.class));

        //then
        assertThatIllegalArgumentException()
            .isThrownBy(validateCallable)
            .withMessageEndingWith("비어있을 수 없습니다.");
    }
}
