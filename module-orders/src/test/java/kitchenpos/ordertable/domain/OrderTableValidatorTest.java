package kitchenpos.ordertable.domain;

import static kitchenpos.ordertable.domain.OrderTableTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.generic.exception.IllegalOperationException;
import kitchenpos.generic.exception.OrderNotCompletedException;
import kitchenpos.generic.exception.OrderTableNotFoundException;

@DisplayName("주문 테이블 밸리데이터 테스트")
@ExtendWith(MockitoExtension.class)
class OrderTableValidatorTest {

    @InjectMocks
    OrderTableValidator validator;

    @Mock
    OrderTableRepository orderTableRepository;
    @Mock
    OrderStatusCheckService orderStatusCheckService;

    @Test
    @DisplayName("상태 변경 실패 - 테이블이 존재하지 않음")
    void changeEmpty_failed1() {
        // given
        OrderTable dummy = new OrderTable();
        when(orderTableRepository.existsById(any())).thenReturn(false);

        // then
        assertThatThrownBy(() -> validator.validateChangeTableStatus(dummy))
            .isInstanceOf(OrderTableNotFoundException.class);
    }

    @Test
    @DisplayName("상태 변경 실패 - 테이블이 그룹에 포함되어 있음")
    void changeEmpty_failed2() {
        // given
        OrderTable table = orderTable(1L, 1L, 6, false);
        when(orderTableRepository.existsById(any())).thenReturn(true);

        // then
        assertThatThrownBy(() -> validator.validateChangeTableStatus(table))
            .isInstanceOf(IllegalOperationException.class);
    }

    @Test
    @DisplayName("상태 변경 실패 - 조리/식사중인 테이블이 존재")
    void changeEmpty_failed3() {
        // given
        OrderTable dummy = new OrderTable();
        when(orderTableRepository.existsById(any())).thenReturn(true);
        when(orderStatusCheckService.existsOrdersInProgress(any())).thenReturn(true);

        // then
        assertThatThrownBy(() -> validator.validateChangeTableStatus(dummy))
            .isInstanceOf(OrderNotCompletedException.class);
    }

    @Test
    @DisplayName("인원 변경 실패 - 테이블이 존재하지 않음")
    void changeNumberOfGuests_failed1() {
        // given
        OrderTable dummy = new OrderTable();
        when(orderTableRepository.existsById(any())).thenReturn(false);

        // then
        assertThatThrownBy(() -> validator.validateChangeNumberOfGuests(dummy))
            .isInstanceOf(OrderTableNotFoundException.class);
    }

    @Test
    @DisplayName("인원 변경 실패 - 테이블이 비어 있음")
    void changeNumberOfGuests_failed2() {
        // given
        OrderTable table = orderTable(1L, 1L, 6, true);
        when(orderTableRepository.existsById(any())).thenReturn(true);

        // then
        assertThatThrownBy(() -> validator.validateChangeNumberOfGuests(table))
            .isInstanceOf(IllegalOperationException.class);
    }
}
