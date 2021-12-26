package kitchenpos.application;

import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.exception.IllegalOrderTablesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @DisplayName("테이블이 1개 있는 테이블 그룹을 생성한다")
    @Test
    void oneOrderTableCreateTest() {
        // given
        TableGroupRequest oneTableTableGroupRequest = new TableGroupRequest(Collections.singletonList(1L));

        // when
        TableGroupService tableGroupService = new TableGroupService(orderTableRepository, tableGroupRepository);

        // then
        assertThatThrownBy(() -> tableGroupService.create(oneTableTableGroupRequest))
                .isInstanceOf(IllegalOrderTablesException.class);
    }

    @DisplayName("테이블이 없는 테이블 그룹을 생성한다")
    @Test
    void noOrderTableCreateTest() {
        // given
        TableGroupRequest nullTableGroupRequest = new TableGroupRequest(Collections.singletonList(null));

        // when
        TableGroupService tableGroupService = new TableGroupService(orderTableRepository, tableGroupRepository);

        // then
        assertThatThrownBy(() -> tableGroupService.create(nullTableGroupRequest))
                .isInstanceOf(IllegalOrderTablesException.class);
    }

    @DisplayName("빈 테이블이 아닌 테이블을 테이블 그룹으로 생성한다")
    @Test
    void noEmptyTableCreateTest() {

        // given
        OrderTable emptyOrderTable = new OrderTable(null, 1, true);
        OrderTable notEmptyOrderTable = new OrderTable(null, 1, false);
        List<OrderTable> orderTables = Arrays.asList(emptyOrderTable, notEmptyOrderTable);
        List<Long> orderTableIds = Arrays.asList(emptyOrderTable.getId(), notEmptyOrderTable.getId());
        TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableIds);
        when(orderTableRepository.findByIdIn(any())).thenReturn(orderTables);

        // when
        TableGroupService tableGroupService = new TableGroupService(orderTableRepository, tableGroupRepository);

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalOrderTablesException.class);
    }

    @DisplayName("테이블 그룹에 속해있는 테이블을 테이블 그룹으로 만든다")
    @Test
    void tableGroupTableCreateTest() {
        // given
        OrderTable tableGroupOrderTable = new OrderTable(new TableGroup(), 1, true);
        OrderTable nullTableGroupOrderTable = new OrderTable(null, 1, true);
        List<OrderTable> orderTables = Arrays.asList(tableGroupOrderTable, nullTableGroupOrderTable);
        List<Long> orderTableIds = Arrays.asList(tableGroupOrderTable.getId(), nullTableGroupOrderTable.getId());
        TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableIds);
        when(orderTableRepository.findByIdIn(any())).thenReturn(orderTables);

        // when
        TableGroupService tableGroupService = new TableGroupService(orderTableRepository, tableGroupRepository);

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalOrderTablesException.class);
    }
}
