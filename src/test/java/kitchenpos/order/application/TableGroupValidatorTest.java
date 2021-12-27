package kitchenpos.order.application;

import kitchenpos.order.application.exception.InvalidOrderState;
import kitchenpos.order.application.exception.InvalidTableState;
import kitchenpos.order.application.exception.TableNotFoundException;
import kitchenpos.order.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("테이블 그룹 조건 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupValidatorTest {
    @Mock
    private TableRepository tableRepository;
    @InjectMocks
    private TableGroupValidator tableGroupValidator;

    private OrderTable 테이블1;
    private OrderTable 테이블2;
    private OrderTable 빈테이블;

    @BeforeEach
    void setUp() {
        테이블1 = OrderTable.of(2, new TableState(false));
        테이블2 = OrderTable.of(2, new TableState(false));
        빈테이블 = OrderTable.of(0, new TableState(true));
    }

    @Test
    @DisplayName("주문 테이블 목록이 2개 미만인 경우 예외가 발생한다.")
    void validateOrderTableSize() {
        List<Long> ids = Collections.singletonList(1L);

        assertThatThrownBy(() -> tableGroupValidator.validate(ids))
                .isInstanceOf(TableNotFoundException.class);
    }

    @Test
    @DisplayName("주문 테이블이 등록되어 있지 않은 경우 예외가 발생한다.")
    void validateOrderTable() {
        when(tableRepository.findById(anyLong())).thenReturn(Optional.of(테이블1));
        when(tableRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tableGroupValidator.validate(Arrays.asList(1L, 2L)))
                .isInstanceOf(TableNotFoundException.class);
        verify(tableRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("주문 테이블이 빈 테이블인 경우 예외가 발생한다.")
    void validateOrderTableEmpty() {
        when(tableRepository.findById(anyLong())).thenReturn(Optional.of(테이블1));
        when(tableRepository.findById(anyLong())).thenReturn(Optional.of(빈테이블));

        assertThatThrownBy(() -> tableGroupValidator.validate(Arrays.asList(1L, 2L)))
                .isInstanceOf(InvalidTableState.class);
        verify(tableRepository, times(2)).findById(anyLong());
    }

    @Test
    @DisplayName("다른 테이블 그룹에 등록되어 있는 주문 테이블인 경우 예외가 발생한다.")
    void validateExistTableGroup() {
        테이블1.group(1L);
        테이블2.group(1L);

        when(tableRepository.findById(anyLong())).thenReturn(Optional.of(테이블1));
        when(tableRepository.findById(anyLong())).thenReturn(Optional.of(테이블2));

        assertThatThrownBy(() -> tableGroupValidator.validate(Arrays.asList(1L, 2L)))
                .isInstanceOf(InvalidTableState.class);
        verify(tableRepository, times(2)).findById(anyLong());
    }

    @Test
    @DisplayName("모든 테이블의 주문 상태가 계산 완료가 아닌 경우 예외가 발생한다.")
    void validateStatusUngroup() {
        테이블1.changeStatus(MEAL);
        테이블2.changeStatus(COMPLETION);

        assertThatThrownBy(() ->  tableGroupValidator.validateTableState(Arrays.asList(테이블1, 테이블2)))
                .isInstanceOf(InvalidOrderState.class);
    }
}
