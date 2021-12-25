package kitchenpos.order.domain;

import static kitchenpos.common.exception.ExceptionMessage.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ExceptionMessage;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.TableGroupValidator;

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

    @DisplayName("주문 테이블이 존재하는지 확인한다.")
    @Test
    void validateExistOrderTable() {
        // given
        List<Long> orderTableIds = findOrderTable.stream().map(OrderTable::getId).collect(Collectors.toList());
        given(orderTableRepository.findAllByIdIn(orderTableIds))
            .willReturn(findOrderTable);

        // when && then
        assertDoesNotThrow(() -> tableGroupValidator.validateOrderTables(orderTableIds));
    }

    @DisplayName("주문 테이블이 존재하지 않으면 단체 지정을 할 수 없다.")
    @Test
    void validateExistOrderTableNotExist() {
        // given
        List<Long> orderTableIds = findOrderTable.stream().map(OrderTable::getId).collect(Collectors.toList());
        given(orderTableRepository.findAllByIdIn(orderTableIds))
            .willReturn(new ArrayList<>());

        // when && then
        assertThatThrownBy(() -> tableGroupValidator.validateOrderTables(orderTableIds))
            .isInstanceOf(BadRequestException.class)
            .hasMessage(WRONG_VALUE.getMessage());
    }

    @DisplayName("단체 지정은 주문테이블이 2개 이상일 경우 가능하다")
    @Test
    void validateOrderTables() {
        // given
        List<Long> orderTableIds = findOrderTable.stream().map(OrderTable::getId).collect(Collectors.toList());
        given(orderTableRepository.findAllByIdIn(orderTableIds))
            .willReturn(findOrderTable);

        // when && then
        assertDoesNotThrow(() -> tableGroupValidator.validateOrderTables(orderTableIds));
    }

    @DisplayName("단체 지정은 주문테이블이 2개 미만일 경우 불가능하다")
    @Test
    void validateOrderTablesLessThanTwo() {
        // given
        List<Long> emptyOrderTableIds = Collections.emptyList();
        List<Long> singleOrderTables = Collections.singletonList(1L);

        given(orderTableRepository.findAllByIdIn(emptyOrderTableIds))
            .willReturn(new ArrayList<>());
        given(orderTableRepository.findAllByIdIn(singleOrderTables))
            .willReturn(Collections.singletonList(OrderTable.of(0, true)));

        // when && then
        assertAll(
            () -> assertThatThrownBy(() -> tableGroupValidator.validateOrderTables(emptyOrderTableIds))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessage.WRONG_VALUE.getMessage()),
            () -> assertThatThrownBy(() -> tableGroupValidator.validateOrderTables(singleOrderTables))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessage.WRONG_VALUE.getMessage())
        );
    }

    @DisplayName("빈테이블이 아니거나 이미 단체로 지정되어있는 테이블은 단체 지정을 할 수 없다")
    @Test
    void validateOrderTableEmptyAndNonExistGroupTable() {
        // given
        List<OrderTable> existOrderTables = Arrays.asList(
            OrderTable.of(1L, 1L, 0, true),
            OrderTable.of(2L, null, 2, true)
        );
        List<Long> existOrderTableIds = existOrderTables.stream()
            .map(OrderTable::getId).collect(Collectors.toList());

        List<OrderTable> notEmptyOrderTables = Arrays.asList(
            OrderTable.of(0, false),
            OrderTable.of(0, true));
        List<Long> notEmptyOrderTableIds = notEmptyOrderTables.stream()
            .map(OrderTable::getId).collect(Collectors.toList());

        given(orderTableRepository.findAllByIdIn(existOrderTableIds))
            .willReturn(existOrderTables);

        given(orderTableRepository.findAllByIdIn(notEmptyOrderTableIds))
            .willReturn(notEmptyOrderTables);

        assertAll(
            () -> assertThatThrownBy(() -> tableGroupValidator.validateOrderTables(existOrderTableIds))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(WRONG_VALUE.getMessage()),

            () -> assertThatThrownBy(() -> tableGroupValidator.validateOrderTables(notEmptyOrderTableIds))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(WRONG_VALUE.getMessage())
        );
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
