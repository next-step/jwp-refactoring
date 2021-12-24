package kitchenpos.ordertable.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.TableGroup;
import kitchenpos.ordertable.domain.TableGroupRepository;
import kitchenpos.ordertable.dto.TableGroupRequest;
import kitchenpos.ordertable.dto.TableGroupResponse;
import kitchenpos.ordertable.testfixtures.TableGroupTestFixtures;
import kitchenpos.ordertable.testfixtures.TableTestFixtures;
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
    private TableService tableService;

    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("테이블 그룹을 등록할 수 있다.")
    @Test
    void create() {
        //given
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, 6, true),
            new OrderTable(2L, 3, true));
        TableTestFixtures.특정_주문테이블_리스트_조회_모킹(tableService, orderTables);

        TableGroupRequest tableGroupRequest = TableGroupTestFixtures.convertToTableGroupRequest(
            orderTables);

        List<OrderTable> expectedOrderTables = Arrays.asList(
            new OrderTable(1L, 6, true),
            new OrderTable(2L, 3, true));
        TableGroup tableGroup = new TableGroup(1L);
        tableGroup.groupTables(expectedOrderTables);
        TableGroupTestFixtures.테이블그룹_저장_결과_모킹(tableGroupRepository, tableGroup);

        //when
        TableGroupResponse savedTableGroup = tableGroupService.create(tableGroupRequest);

        //then
        assertThat(savedTableGroup.getCreatedDate()).isEqualTo(tableGroup.getCreatedDate());
        assertThat(savedTableGroup.getOrderTables().size()).isEqualTo(
            tableGroup.getOrderTableSize());
    }

    @DisplayName("테이블 그룹을 해제할 수 있다.")
    @Test
    void ungroup() {
        //given
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, 6, true),
            new OrderTable(2L, 3, true));
        TableGroup tableGroup = new TableGroup(1L);
        tableGroup.groupTables(orderTables);
        TableGroupTestFixtures.특정_테이블그룹_조회_모킹(tableGroupRepository, tableGroup);

        //when
        tableGroupService.ungroup(tableGroup.getId());

        //then
        orderTables.stream()
            .forEach(orderTable -> assertThat(orderTable.getTableGroup()).isNull());
    }
}
