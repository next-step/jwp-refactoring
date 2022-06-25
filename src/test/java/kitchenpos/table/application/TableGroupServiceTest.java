package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.infrastructure.OrderTableRepository;
import kitchenpos.table.infrastructure.TableGroupRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
    @Mock
    private TableGroupRepository tableGroupRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    @Test
    @DisplayName("주문 테이블이 하나인 경우, 단체지정할 수 없다.")
    void createWithOneOrderTable() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(4, false);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Lists.list(orderTableRequest));

        assertThatThrownBy(() -> {
            tableGroupService.create(tableGroupRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 없는 경우, 단체지정할 수 없다.")
    void createWithNoExistingOrderTables() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(4, false);
        OrderTableRequest secondOrderTableRequest = new OrderTableRequest(2, false);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Lists.list(orderTableRequest, secondOrderTableRequest));
        when(orderTableRepository.findAllByIdIn(any())).thenReturn(Lists.emptyList());

        assertThatThrownBy(() -> {
            tableGroupService.create(tableGroupRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미 단체 지정된 경우, 단체지정할 수 없다.")
    void createWithAlreadyGrouped() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(4, true);
        OrderTableRequest secondOrderTableRequest = new OrderTableRequest(2, true);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Lists.list(orderTableRequest, secondOrderTableRequest));
        OrderTable table = OrderTable.of(4, true);
        OrderTable secondTable = OrderTable.of(2, true);
        TableGroup tableGroup = new TableGroup();
        table.registerGroupTable(tableGroup);
        secondTable.registerGroupTable(tableGroup);
        when(orderTableRepository.findAllByIdIn(any())).thenReturn(Lists.list(table, secondTable));

        assertThatThrownBy(() -> {
            tableGroupService.create(tableGroupRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 테이블이 아닌 경우, 단체지정할 수 없다.")
    void createWithNotEmptyTable() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(4, false);
        OrderTableRequest secondOrderTableRequest = new OrderTableRequest(2, false);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Lists.list(orderTableRequest, secondOrderTableRequest));
        OrderTable table = OrderTable.of(4, false);
        OrderTable secondTable = OrderTable.of(2, false);
        when(orderTableRepository.findAllByIdIn(any())).thenReturn(Lists.list(table, secondTable));

        assertThatThrownBy(() -> {
            tableGroupService.create(tableGroupRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체지정할 수 있다.")
    void create() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(4, true);
        OrderTableRequest secondOrderTableRequest = new OrderTableRequest(2, true);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Lists.list(orderTableRequest, secondOrderTableRequest));
        OrderTable table = OrderTable.of(4, true);
        OrderTable secondTable = OrderTable.of(2, true);
        TableGroup tableGroup = new TableGroup();
        when(orderTableRepository.findAllByIdIn(any())).thenReturn(Lists.list(table, secondTable));
        when(tableGroupRepository.save(any())).thenReturn(tableGroup);

        tableGroupService.create(tableGroupRequest);

        assertThat(table.getTableGroup()).isEqualTo(tableGroup);
        assertThat(secondTable.getTableGroup()).isEqualTo(tableGroup);
    }

//    @Test
//    @DisplayName("테이블이 조리, 식사 중이면 단체 지정을 해제할 수 없다.")
//    void unGroupWithInvalidOrderStatus() {
//        //given
//        TableGroup saved = tableGroupService.create(secondTableGroup);
//        //when, then
//        assertThatThrownBy(() -> {
//            tableGroupService.ungroup(saved.getId());
//        }).isInstanceOf(IllegalArgumentException.class);
//    }

    @Test
    @DisplayName("단체 지정을 해제할 수 있다.")
    void unGroup() {
        OrderTable table = OrderTable.of(4, true);
        OrderTable secondTable = OrderTable.of(2, true);
        when(orderTableRepository.findAllByTableGroupId(any())).thenReturn(Lists.list(table, secondTable));

        tableGroupService.ungroup(1L);

        assertThat(table.getTableGroup()).isNull();
        assertThat(secondTable.getTableGroup()).isNull();
    }
}
