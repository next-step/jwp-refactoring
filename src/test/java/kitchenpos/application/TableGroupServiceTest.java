package kitchenpos.application;

import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.exception.IllegalOrderTablesException;
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
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Collections.singletonList(1L));

        // when
        TableGroupService tableGroupService = new TableGroupService(orderTableRepository, tableGroupRepository);

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest)).isInstanceOf(IllegalOrderTablesException.class);
    }

    @DisplayName("테이블이 없는 테이블 그룹을 생성한다")
    @Test
    void noOrderTableCreateTest() {
        // given
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Collections.singletonList(null));

        // when
        TableGroupService tableGroupService = new TableGroupService(orderTableRepository, tableGroupRepository);

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest)).isInstanceOf(IllegalOrderTablesException.class);
    }

    @DisplayName("빈 테이블이 아닌 테이블을 테이블 그룹으로 생성한다")
    @Test
    void noEmptyTableCreateTest() {

        // given
        OrderTable orderTable1 = new OrderTable(null, 1, true);
        OrderTable orderTable2 = new OrderTable(null, 1, false);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        List<Long> orderTableIds = Arrays.asList(orderTable1.getId(), orderTable2.getId());
        TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableIds);
        when(orderTableRepository.findByIdIn(any())).thenReturn(orderTables);

        // when
        TableGroupService tableGroupService = new TableGroupService(orderTableRepository, tableGroupRepository);

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest)).isInstanceOf(IllegalOrderTablesException.class);
    }

    @DisplayName("테이블 그룹에 속해있는 테이블을 테이블 그룹으로 만든다")
    @Test
    void tableGroupTableCreateTest() {
        // given
        OrderTable orderTable1 = new OrderTable(new TableGroup(), 1, true);
        OrderTable orderTable2 = new OrderTable(null, 1, true);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        List<Long> orderTableIds = Arrays.asList(orderTable1.getId(), orderTable2.getId());
        TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableIds);
        when(orderTableRepository.findByIdIn(any())).thenReturn(orderTables);

        // when
        TableGroupService tableGroupService = new TableGroupService(orderTableRepository, tableGroupRepository);

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest)).isInstanceOf(IllegalOrderTablesException.class);
    }
}
