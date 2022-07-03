package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.application.TableGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.factory.fixture.OrderTableFixtureFactory.createOrderTable;
import static kitchenpos.factory.fixture.TableGroupFixtureFactory.createTableGroup;
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

    private OrderTable firstOrderTable;
    private OrderTable secondOrderTable;
    private List<OrderTable> orderTables;
    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        firstOrderTable = createOrderTable(1L, true);
        secondOrderTable = createOrderTable(2L, true);
        orderTables = Arrays.asList(firstOrderTable, secondOrderTable);
        tableGroup = createTableGroup(LocalDateTime.now(), orderTables);
    }

    @DisplayName("테이블 그룹을 생성할 수 있다.")
    @Test
    void create() {
        given(orderTableDao.findAllByIdIn(Arrays.asList(firstOrderTable.getId(), secondOrderTable.getId())))
                .willReturn(orderTables);
        given(tableGroupDao.save(any(TableGroup.class))).willReturn(tableGroup);

        TableGroup result = tableGroupService.create(tableGroup);

        assertThat(result.getOrderTables()).hasSize(2);
    }

    @DisplayName("주문 테이블이 2개 이하이면 테이블 그룹을 생성할 수 없다.")
    @Test
    void create_invalid_orderTables_size() {
        tableGroup.setOrderTables(Arrays.asList());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록된 주문 테이블을 사용하지 않는다면 테이블 그룹을 생성할 수 없다.")
    @Test
    void create_invalid_orderTables_id() {
        given(orderTableDao.findAllByIdIn(Arrays.asList(firstOrderTable.getId(), secondOrderTable.getId())))
                .willReturn(Arrays.asList(firstOrderTable));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록된 주문 테이블이 비어있다면, 테이블 그룹을 생성할 수 없다.")
    @Test
    void create_empty_orderTables() {
        firstOrderTable.setEmpty(false);
        secondOrderTable.setEmpty(false);

        given(orderTableDao.findAllByIdIn(Arrays.asList(firstOrderTable.getId(), secondOrderTable.getId())))
                .willReturn(Arrays.asList(firstOrderTable));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록된 주문 테이블에 테이블그룹아이디가 설정되어 있다면, 테이블 그룹을 생성할 수 없다.")
    @Test
    void create_nonNull_orderTables() {
        firstOrderTable.setTableGroupId(1L);

        given(orderTableDao.findAllByIdIn(Arrays.asList(firstOrderTable.getId(), secondOrderTable.getId())))
                .willReturn(Arrays.asList(firstOrderTable));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 해제할 수 있다.")
    @Test
    void unGroup() {
        firstOrderTable.setTableGroupId(1L);
        secondOrderTable.setTableGroupId(1L);
        given(orderTableDao.findAllByTableGroupId(1L)).willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(Boolean.FALSE);

        tableGroupService.ungroup(1L);

        assertThat(firstOrderTable.getTableGroupId()).isNull();
    }

    @DisplayName("주문 상태가 COOKING 이거나 MEAL 이면, 테이블 그룹을 해제할 수 없다.")
    @Test
    void unGroup_invalid_orderStatus() {
        given(orderTableDao.findAllByTableGroupId(1L)).willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(Boolean.TRUE);

        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
