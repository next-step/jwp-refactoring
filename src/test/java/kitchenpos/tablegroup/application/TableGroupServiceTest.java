package kitchenpos.tablegroup.application;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


@DisplayName("테이블 그룹 서비스 관련 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private TableGroupRepository tableGroupRepository;

    private TableGroupService tableGroupService;
    private TableGroup tableGroup;
    private OrderTable orderTable;
    private OrderTable orderTable2;
    private TableGroupRequest tableGroupRequest;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(tableGroupRepository);
        orderTable = new OrderTable(1L, null, 0, true);
        orderTable2 = new OrderTable(2L, null, 0, true);
        tableGroup = new TableGroup(1L, Lists.newArrayList(orderTable, orderTable2));

        tableGroupRequest = new TableGroupRequest(Lists.newArrayList(orderTable, orderTable2));
    }

    @DisplayName("상품 등록 테스트")
    @Test
    void createTableGroupTest() {
        when(tableGroupRepository.save(any())).thenReturn(tableGroup);

        // when
        final TableGroupResponse createdTableGroup = tableGroupService.create(tableGroupRequest);

        // then
        assertAll(
                () -> assertThat(createdTableGroup.getOrderTables().get(0)).isEqualTo(orderTable),
                () -> assertThat(createdTableGroup.getOrderTables().get(1)).isEqualTo(orderTable2)
        );
    }

    @DisplayName("테이블 그룹 생성 시 주문 테이블은 2개 이상이어야 한다.")
    @Test
    void createTableGroupExistMultipleOrderTableExceptionTest() {
        assertThatThrownBy(() -> {
            // when
            final TableGroupResponse createdTableGroup = tableGroupService.create(new TableGroupRequest());

            // then
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 생성 시 주문 테이블은 비어있어야 한다.")
    @Test
    void createTableGroupEmptyOrderTablesFromDBExceptionTest() {
        assertThatThrownBy(() -> {
            TableGroupRequest notEmptyOrderTables = new TableGroupRequest(Lists.newArrayList(new OrderTable(1L, null, 0, false), new OrderTable(1L, null, 0, false)));

            // when
            final TableGroupResponse createdTableGroup = tableGroupService.create(notEmptyOrderTables);

            // then
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹에 주문 테이블들을 등록한다.")
    @Test
    void createOrderTableInTableGroupTest() {
        when(tableGroupRepository.save(any())).thenReturn(tableGroup);

        // when
        final TableGroupResponse createdTableGroup = tableGroupService.create(tableGroupRequest);

        // then
        assertAll(
                () -> assertThat(createdTableGroup.getOrderTables().get(0)).isEqualTo(orderTable),
                () -> assertThat(createdTableGroup.getOrderTables().get(1)).isEqualTo(orderTable2)
        );
    }

    @DisplayName("테이블 그룹은 주문 상태가 cooking or meal 이 아니어야 한다.")
    @Test
    void ungroupTableGroupIsNotMealOrCookingStatusTest() {
        assertThatThrownBy(() -> {
            when(tableGroupRepository.save(any())).thenReturn(tableGroup);

            // given
            tableGroupService.create(tableGroupRequest);

            // when
            tableGroupService.ungroup(1L);

        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블에 테이블 그룹을 해제한다.")
    @Test
    void ungroupTableGroupTest() {
        when(tableGroupRepository.save(any())).thenReturn(tableGroup);
        when(tableGroupRepository.findById(anyLong())).thenReturn(Optional.ofNullable(tableGroup));

        // given
        TableGroupResponse createdTableGroup = tableGroupService.create(tableGroupRequest);

        // when
        tableGroupService.ungroup(createdTableGroup.getId());

        // then
        assertThat(orderTable.getTableGroup()).isEqualTo(null);
    }
}