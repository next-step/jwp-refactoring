package kitchenpos.table_group.application;

import kitchenpos.table.application.TableService;
import kitchenpos.table_group.domain.GroupTable;
import kitchenpos.table_group.domain.TableGroup;
import kitchenpos.table_group.domain.TableGroupValidator;
import kitchenpos.table_group.repository.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("단체 지정 관련")
@SpringBootTest
class TableGroupServiceTest {
    @Autowired
    TableGroupService tableGroupService;
    @MockBean
    TableGroupRepository tableGroupRepository;
    @MockBean
    TableGroupValidator tableGroupValidator;
    @MockBean
    TableService tableService;

    @DisplayName("여러 주문 테이블을 단체 지정할 수 있다")
    @Test
    void create() {
        // given
        List<Long> orderTableIds = Arrays.asList(1L, 2L);
        List<GroupTable> groupTables = Arrays.asList(new GroupTable(1L, false, true), new GroupTable(2L, false, true));
        when(tableGroupValidator.checkOrderTableIds(orderTableIds)).thenReturn(groupTables);
        when(tableGroupRepository.save(any(TableGroup.class))).thenReturn(new TableGroup());

        // when
        tableGroupService.create(orderTableIds);

        // then
        verify(tableService).groupBy(any(), eq(orderTableIds));
    }

    @DisplayName("단체 지정을 해제할 수 있다")
    @Test
    void ungroup() {
        // given
        Long tableGroupId = 1L;
        TableGroup tableGroup = new TableGroup();
        when(tableGroupRepository.findById(tableGroupId)).thenReturn(Optional.of(tableGroup));

        // when
        tableGroupService.ungroup(tableGroupId);

        // then
        verify(tableGroupValidator).checkUngroupTables(tableGroup);
        verify(tableService).ungroup(tableGroupId);
    }

    @DisplayName("단체 지정을 해제할 수 없다")
    @Test
    void tableGroupNotExist() {
        // given
        Long tableGroupId = 1L;

        // when then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
