package kitchenpos.application;

import static java.util.Arrays.*;
import static kitchenpos.domain.OrderStatus.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private TableService tableService;

    @DisplayName("등록되지않은 주문테이블 id 는 빈 테이블 상태를 변경할 수 없다")
    @Test
    void table1() {
        assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTable()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체지정된 주문테이블은 빈 테이블 상태를 변경할 수 없다")
    @Test
    void table2() {
        OrderTable orderTable = new OrderTable(10L, 10, false);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문상태가 요리중,식사중인 경우 빈 테이블 상태를 변경할 수 없다")
    @Test
    void table3() {
        OrderTable orderTable = new OrderTable(null, 10, false);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(orderTable));
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), eq(asList(COOKING.name(), MEAL.name()))))
            .thenReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문손님수는 1명 이상만 가능하다")
    @Test
    void table4() {
        OrderTable orderTable = new OrderTable(null, -10, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블인 경우 방문손님수 변경할 수 없다")
    @Test
    void table5() {
        OrderTable orderTable = new OrderTable(null, 10, true);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
