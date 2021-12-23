package kichenpos.order.order.domain;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import feign.FeignException;
import kichenpos.order.table.infrastructure.OrderTableClient;
import kichenpos.order.table.infrastructure.dto.OrderTableDto;
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
    OrderTableClient tableClient;

    @InjectMocks
    OrderValidator validator;

    @Test
    @DisplayName("검증")
    void validate() {
        //given
        when(tableClient.getTable(anyLong()))
            .thenReturn(new OrderTableDto(false));

        //when
        ThrowingCallable validateCallable = () -> validator.validate(mock(Order.class));

        //then
        assertThatNoException().isThrownBy(validateCallable);
    }


    @Test
    @DisplayName("주문 테이블은 채워져 있어야 함")
    void validate_emptyTable_thrownIllegalArgumentException() {
        //given
        when(tableClient.getTable(anyLong()))
            .thenReturn(new OrderTableDto(true));

        //when
        ThrowingCallable validateCallable = () -> validator.validate(mock(Order.class));

        //then
        assertThatIllegalArgumentException()
            .isThrownBy(validateCallable)
            .withMessageEndingWith("비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("등록하려는 주문의 테이블 정보는 조회할 수 있어야 함")
    void validate_notExistsOrderTable_thrownException() {
        //given
        doThrow(FeignException.class)
            .when(tableClient)
            .getTable(anyLong());

        //when
        ThrowingCallable validateCallable = () -> validator.validate(mock(Order.class));

        //then
        assertThatExceptionOfType(FeignException.class).isThrownBy(validateCallable);
    }
}
