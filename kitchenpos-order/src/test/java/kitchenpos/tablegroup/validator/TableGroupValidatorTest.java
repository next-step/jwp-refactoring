package kitchenpos.tablegroup.validator;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.fixture.TestOrderFactory;
import kitchenpos.order.domain.Order;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.domain.NumberOfGuests;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@DisplayName("단체 그룹 Validator 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupValidatorTest {
    @InjectMocks
    private TableGroupValidator tableGroupValidator;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @DisplayName("단체 그룹 등록시 정상적으로 유효성 검사가 성공 된다")
    @Test
    void validateCreateTableGroup() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, new NumberOfGuests(4), true);
        OrderTable orderTable2 = new OrderTable(2L, new NumberOfGuests(4), true);
        TableGroupRequest request =
                TableGroupRequest.of(Arrays.asList(orderTable1.getId(), orderTable2.getTableGroupId()));

        when(orderTableRepository.findAllById(anyList())).thenReturn(Arrays.asList(orderTable1, orderTable2));

        // when & then
        assertDoesNotThrow(() -> tableGroupValidator.validateCreateTableGroup(request));
    }

    @DisplayName("단체 그룹 등록시 테이블이 비어있다면 예외가 발생한다.")
    @Test
    void validateEmptyTable() {
        // given
        TableGroupRequest request = TableGroupRequest.of(new ArrayList<>());

        // when & then
        assertThatThrownBy(() -> tableGroupValidator.validateCreateTableGroup(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.ORDER_TABLES_IS_EMPTY.getMessage());
    }

    @DisplayName("단체 그룹 등록시 테이블이 2개 미만일 경우 예외가 발생한다.")
    @Test
    void validateMinimumTable() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, new NumberOfGuests(4), true);
        TableGroupRequest request =
                TableGroupRequest.of(Arrays.asList(orderTable1.getId()));

        // when & then
        assertThatThrownBy(() -> tableGroupValidator.validateCreateTableGroup(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.ORDER_TABLES_MINIMUM_IS_TWO.getMessage());
    }

    @DisplayName("단체 그룹 등록시 조회된 테이블 개수가 다른 경우 예외가 발생한다.")
    @Test
    void validateUnMatchTableCount() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, new NumberOfGuests(4), true);
        OrderTable orderTable2 = new OrderTable(2L, new NumberOfGuests(4), true);
        TableGroupRequest request =
                TableGroupRequest.of(Arrays.asList(orderTable1.getId(), orderTable2.getTableGroupId()));
        when(orderTableRepository.findAllById(anyList())).thenReturn(Arrays.asList(orderTable1));

        // when & then
        assertThatThrownBy(() -> tableGroupValidator.validateCreateTableGroup(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.ORDER_TABLE_IS_NOT_EXIST.getMessage());
    }

    @DisplayName("단체 그룹 등록시 테이블이 빈 상태가 아니라면 예외가 발생한다.")
    @Test
    void validateNotEmptyTable() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, new NumberOfGuests(4), false);
        OrderTable orderTable2 = new OrderTable(2L, new NumberOfGuests(4), false);
        TableGroupRequest request =
                TableGroupRequest.of(Arrays.asList(orderTable1.getId(), orderTable2.getTableGroupId()));

        when(orderTableRepository.findAllById(anyList())).thenReturn(Arrays.asList(orderTable1, orderTable2));

        // when & then
        assertThatThrownBy(() -> tableGroupValidator.validateCreateTableGroup(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.NOT_EMPTY_STATUS_IN_ORDER_TABLES.getMessage());
    }

    @DisplayName("단체 그룹 등록시 테이블이 다른 단체 그룹에 등록되어 있다면 예외가 발생한다.")
    @Test
    void validateAlreadyGroupTable() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, new NumberOfGuests(4), true);
        OrderTable orderTable2 = new OrderTable(2L, new NumberOfGuests(4), true);
        TableGroupRequest request =
                TableGroupRequest.of(Arrays.asList(orderTable1.getId(), orderTable2.getTableGroupId()));

        orderTable1.setTableGroupId(1L);
        when(orderTableRepository.findAllById(anyList())).thenReturn(Arrays.asList(orderTable1, orderTable2));

        // when & then
        assertThatThrownBy(() -> tableGroupValidator.validateCreateTableGroup(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.ORDER_TABLES_HAS_GROUP_TABLE.getMessage());
    }

    @DisplayName("단체 그룹 해제시 정상적으로 유효성 검사가 성공 된다")
    @Test
    void validateUnGroup() {
        // given
        OrderTable orderTable = new OrderTable(1L, new NumberOfGuests(4), true);
        Order order = TestOrderFactory.createCompleteOrder();
        TableGroup tableGroup = new TableGroup(1L);
        when(orderTableRepository.findAllByTableGroupId(tableGroup.getId())).thenReturn(Arrays.asList(orderTable));
        when(orderRepository.findAllByOrderTableIdIn(Arrays.asList(orderTable.getId())))
                .thenReturn(Arrays.asList(order));

        // when & then
        assertDoesNotThrow(() -> tableGroupValidator.validateUnGroup(tableGroup));
    }

    @DisplayName("단체 그룹 해제시 주문이 계산완료 상태가 아니라면 예외가 발생한다.")
    @Test
    void validateNotCompleteUnGroup() {
        // given
        OrderTable orderTable = new OrderTable(1L, new NumberOfGuests(4), true);
        Order order = TestOrderFactory.createMealOrder();
        TableGroup tableGroup = new TableGroup(1L);
        when(orderTableRepository.findAllByTableGroupId(tableGroup.getId())).thenReturn(Arrays.asList(orderTable));
        when(orderRepository.findAllByOrderTableIdIn(Arrays.asList(orderTable.getId())))
                .thenReturn(Arrays.asList(order));

        // when & then
        assertThatThrownBy(() -> tableGroupValidator.validateUnGroup(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.ORDER_STATUS_NOT_COMPLETE.getMessage());
    }
}
