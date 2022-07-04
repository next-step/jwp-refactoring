package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import kitchenpos.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableValidatorTest {
    private TableValidator tableValidator;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        tableValidator = new TableValidator(orderRepository);
    }

    @Test
    @DisplayName("식사가 완료되지 않은 테이블들이 존재하면 에러 발생")
    void existNotCompletionOrderTables() {
        // given
        when(orderRepository.existNotCompletionOrderTables(any())).thenReturn(true);
        // when && then
        assertThatIllegalStateException()
                .isThrownBy(() -> tableValidator.validateNotCompletionOrderTables(any()));
    }

    @Test
    @DisplayName("식사가 완료된 상태의 테이블들만 존재한다")
    void existCompletionOrderTables() {
        // given
        when(orderRepository.existNotCompletionOrderTables(any())).thenReturn(false);
        // when && then
        tableValidator.validateNotCompletionOrderTables(any());
    }

    @Test
    @DisplayName("식사가 완료되지 않은 테이블이 존재하면 에러 발생")
    void existNotCompletionOrderTable() {
        // given
        when(orderRepository.existNotCompletionOrderTable(any())).thenReturn(true);
        // when && then
        assertThatIllegalStateException()
                .isThrownBy(() -> tableValidator.validateNotCompletionOrderTable(any()));
    }

    @Test
    @DisplayName("식사가 완료된 상태의 테이블만 존재한다")
    void existCompletionOrderTable() {
        // given
        when(orderRepository.existNotCompletionOrderTable(any())).thenReturn(false);
        // when && then
        tableValidator.validateNotCompletionOrderTable(any());
    }
}
