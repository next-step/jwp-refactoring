package kitchenpos.order.domain;

import static kitchenpos.common.exception.ExceptionMessage.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.OrderTableRepository;

@ExtendWith(MockitoExtension.class)
class TableGroupValidatorTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderRepository orderRepository;

    private TableGroupValidator tableGroupValidator;

    private List<OrderTable> findOrderTable;

    private List<OrderStatus> notCompletionOrderStatuses;

    @BeforeEach
    void setUp() {
        tableGroupValidator = new TableGroupValidator(orderTableRepository, orderRepository);
        findOrderTable = Arrays.asList(
            OrderTable.of(1L, null, 0, true),
            OrderTable.of(2L, null, 2, true)
        );
        notCompletionOrderStatuses = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);
    }

    @DisplayName("주문이 계산완료 상태여야 주문테이블을 단체 지정에서 해지할 수 있다.")
    @Test
    void validateUngroup() {
        // given
        Long tableGroupId = 1L;
        given(orderTableRepository.findAllByTableGroupId(tableGroupId))
            .willReturn(findOrderTable);
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(findOrderTable.stream()
            .map(OrderTable::getId).collect(Collectors.toList()), notCompletionOrderStatuses)).willReturn(false);

        // when && then
        assertDoesNotThrow(() -> tableGroupValidator.validateUngroup(tableGroupId));
    }

    @DisplayName("주문이 계산완료 상태가 아니면 주문테이블을 단체 지정에서 해지할 수 없다.")
    @Test
    void validateUngroupNotCompletionOrderStatus() {
        // given
        Long tableGroupId = 1L;
        given(orderTableRepository.findAllByTableGroupId(tableGroupId))
            .willReturn(findOrderTable);
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(findOrderTable.stream()
            .map(OrderTable::getId).collect(Collectors.toList()), notCompletionOrderStatuses)).willReturn(true);

        // when && then
        assertThatThrownBy(() -> tableGroupValidator.validateUngroup(tableGroupId))
            .isInstanceOf(BadRequestException.class)
            .hasMessage(CANNOT_CHANGE_STATUS.getMessage());
    }

}
