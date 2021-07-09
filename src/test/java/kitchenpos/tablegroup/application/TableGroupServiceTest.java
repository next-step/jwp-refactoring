package kitchenpos.tablegroup.application;

import kitchenpos.order.domain.OrderDao;
import kitchenpos.table.domain.OrderTableDao;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroupDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable orderTable1;
    private OrderTable orderTable2;
    private List<OrderTable> orderTables;

    @BeforeEach
    void setUp() {
        orderTable1 = new OrderTable(1L, null, 2, true);
        orderTable2 = new OrderTable(2L, null, 3, true);
        orderTables = new ArrayList<>();
        orderTables.add(orderTable1);
        orderTables.add(orderTable2);
    }

    @DisplayName("테이블 그룹을 등록한다.")
    @Test
    void create() {
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);
        given(tableGroupDao.save(any())).willReturn(tableGroup);
        given(orderTableDao.save(orderTable1)).willReturn(orderTable1);
        given(orderTableDao.save(orderTable2)).willReturn(orderTable2);

        TableGroup created = tableGroupService.create(tableGroup);

        assertThat(created).isEqualTo(tableGroup);
        assertThat(created.getOrderTables()).containsExactly(orderTable1, orderTable2);
    }

    @DisplayName("테이블 그룹 등록을 실패한다 - 등록에 요청할 주문 테이블 갯수가 없거나 2개 미만일 경우 실패")
    @Test
    void fail_create1() {
        List<OrderTable> orderTables = Arrays.asList(orderTable1);
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);
        TableGroup nonetableGroup = new TableGroup(1L, LocalDateTime.now(), Collections.emptyList());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> tableGroupService.create(nonetableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 등록을 실패한다 - 주문 테이블이 사전에 등록(주문값이 있는 상태) 되어 있지 않을 경우 실패")
    @Test
    void fail_create2() {
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Collections.emptyList());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 등록을 실패한다 -  주문 테이블은 empty 값이 false일 경우 실패")
    @Test
    void fail_create3() {
        OrderTable orderTable1 = new OrderTable(1L, null, 2, false);
        OrderTable orderTable2 = new OrderTable(2L, null, 3, true);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("테이블 그룹 등록을 실패한다 -  주문 테이블 groupId 값이 있을 경우 실패")
    @Test
    void fail_create4() {
        OrderTable orderTable1 = new OrderTable(1L, 2L, 2, true);
        OrderTable orderTable2 = new OrderTable(2L, null, 3, true);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 등록해제(ungroup) 한다.")
    @Test
    void ungroup() {
        OrderTable groupedTable1 = new OrderTable(1L, 1L, 2, true);
        OrderTable groupedTable2 = new OrderTable(1L, 1L, 2, true);
        List<OrderTable> groupedTables = Arrays.asList(groupedTable1, groupedTable2);
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), groupedTables);
        given(orderTableDao.findAllByTableGroupId(tableGroup.getId())).willReturn(groupedTables);
        given(orderTableDao.save(groupedTable1)).willReturn(groupedTable1);
        given(orderTableDao.save(groupedTable2)).willReturn(groupedTable2);

        tableGroupService.ungroup(tableGroup.getId());

        assertThat(tableGroup.getOrderTables().get(0).getTableGroupId()).isNull();
    }

    @DisplayName("테이블 그룹 등록해제를 실패 한다. - 그룹된 주문 테이블이 조리중이거나, 식사중일때에는 그룹 해제 불가")
    @Test
    void fail_ungroup() {
        OrderTable groupedTable1 = new OrderTable(1L, 1L, 2, true);
        OrderTable groupedTable2 = new OrderTable(1L, 1L, 2, true);
        List<OrderTable> groupedTables = Arrays.asList(groupedTable1, groupedTable2);
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), groupedTables);
        given(orderTableDao.findAllByTableGroupId(tableGroup.getId())).willReturn(groupedTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
