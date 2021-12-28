package kitchenpos.tablegroup.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroupId;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.table.dto.OrderTableDto;
import kitchenpos.tablegroup.dto.TableGroupDto;
import kitchenpos.tablegroup.exception.NotFoundTableGroupException;
import kitchenpos.tablegroup.domain.TableGroupValidator;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
    @Mock
    private TableGroupRepository tableGroupRepository;
    
    @Mock
    private TableGroupValidator tableGroupValidator;
    
    @Mock
    private TableService tableService;

    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("단체지정이 저장된다.")
    @Test
    void create_tableGroup() {
        // given
        OrderTable 치킨_주문_단체테이블 = OrderTable.of(0, true);
        OrderTable 치킨2_주문_단체테이블 = OrderTable.of(0, true);

        when(tableService.findAllByIdIn(anyList())).thenReturn(List.of(치킨_주문_단체테이블, 치킨2_주문_단체테이블));
        when(tableGroupRepository.save(any(TableGroup.class))).thenReturn(TableGroup.of(1L));

        TableGroupDto 단체지정_요청전문 = TableGroupDto.of(List.of(OrderTableDto.of(치킨_주문_단체테이블), OrderTableDto.of(치킨2_주문_단체테이블)));

        // when
        TableGroupDto tableGroupDto = tableGroupService.create(단체지정_요청전문);

        // then
        Assertions.assertThat(tableGroupDto.getOrderTables().get(0).getTableGroupId()).isEqualTo(1L);
        Assertions.assertThat(tableGroupDto.getOrderTables().get(1).getTableGroupId()).isEqualTo(1L);
    }

    @DisplayName("단체지정이 해제된다.")
    @Test
    void update_tableUnGroup() {
        // given
        OrderTable 치킨_주문_단체테이블 = OrderTable.of(10, false);
        OrderTable 치킨2_주문_단체테이블 = OrderTable.of(10, false);

        OrderTables 주문테이블들 = OrderTables.of(Lists.newArrayList(치킨_주문_단체테이블, 치킨2_주문_단체테이블));
        치킨_주문_단체테이블.changeEmpty(true);
        치킨2_주문_단체테이블.changeEmpty(true);

        TableGroup 단체주문테이블 = TableGroup.of(1L);
        주문테이블들.groupingTable(TableGroupId.of(단체주문테이블.getId()));

        when(tableGroupRepository.findById(nullable(Long.class))).thenReturn(Optional.of(단체주문테이블));
        when(tableService.findByTableGroupId(nullable(Long.class))).thenReturn(List.of(치킨_주문_단체테이블, 치킨2_주문_단체테이블));

        // when
        tableGroupService.ungroup(단체주문테이블.getId());

        // then
        Assertions.assertThat(치킨_주문_단체테이블.getTableGroupId()).isNull();
        Assertions.assertThat(치킨2_주문_단체테이블.getTableGroupId()).isNull();
    }

    @DisplayName("단체지정해제시 조회된 단체지정이 없으면 에러가 발생된다.")
    @Test
    void exception_updateTableUnGroup_notFound() {
        // given
        OrderTable 치킨_주문_단체테이블 = OrderTable.of(10, false);
        OrderTable 치킨2_주문_단체테이블 = OrderTable.of(10, false);

        OrderTables 주문테이블들 = OrderTables.of(Lists.newArrayList(치킨_주문_단체테이블, 치킨2_주문_단체테이블));
        치킨_주문_단체테이블.changeEmpty(true);
        치킨2_주문_단체테이블.changeEmpty(true);

        TableGroup 단체주문테이블 = TableGroup.of(1L);
        주문테이블들.groupingTable(TableGroupId.of(단체주문테이블.getId()));

        when(tableGroupRepository.findById(nullable(Long.class))).thenReturn(Optional.empty());

        // when
        // then
        Assertions.assertThatExceptionOfType(NotFoundTableGroupException.class)
                    .isThrownBy(() -> tableGroupService.ungroup(단체주문테이블.getId()));
    }
}
