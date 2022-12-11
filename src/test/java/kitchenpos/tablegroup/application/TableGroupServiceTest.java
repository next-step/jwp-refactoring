package kitchenpos.tablegroup.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.tablegroup.domain.GroupTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.domain.TableGroupValidator;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private TableGroupRepository tableGroupRepository;

    @Mock
    private TableGroupValidator tableGroupValidator;

    @InjectMocks
    private TableGroupService tableGroupService;

    GroupTable tableA;
    GroupTable tableB;

    @BeforeEach
    void setUp() {
        tableA = new GroupTable(1L, 1L, 0, true);
        tableB = new GroupTable(1L, 1L, 0, true);
    }

    @DisplayName("테이블 그룹을 추가할 수 있다.")
    @Test
    void create() {
        List<Long> tableIds = Arrays.asList(1L, 2L);
        List<GroupTable> groupTables = Arrays.asList(tableA, tableB);
        given(tableGroupRepository.save(any())).willReturn(new TableGroup());
        given(tableGroupValidator.groupingTable(tableIds, null)).willReturn(groupTables);

        TableGroupResponse actual = tableGroupService.create(tableIds);

        assertThat(actual.getOrderTables()).hasSize(2);
    }

    @DisplayName("테이블 그룹을 해제할 수 있다.")
    @Test
    void ungroup() {
        given(tableGroupRepository.findById(any())).willReturn(Optional.of(new TableGroup()));

        tableGroupService.ungroup(any());

        verify(tableGroupValidator).ungroup(any());
        verify(tableGroupRepository).deleteById(any());
    }
}
