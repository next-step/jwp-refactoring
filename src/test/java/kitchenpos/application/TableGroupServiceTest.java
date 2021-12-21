package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@DisplayName("테이블 그룹 서비스 관련 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    private TableGroupService tableGroupService;
    private TableGroup tableGroup;
    private OrderTable orderTable;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
        tableGroup = new TableGroup();
        orderTable = new OrderTable(1L, null, 0, true);
        orderTable2 = new OrderTable(2L, null, 0, true);
        tableGroup.setOrderTables(Lists.newArrayList(orderTable, orderTable2));
    }

    @DisplayName("상품 등록 테스트")
    @Test
    void createTableGroupTest() {
        when(orderTableDao.findAllByIdIn(any())).thenReturn(Lists.newArrayList(orderTable, orderTable2));
        when(tableGroupDao.save(any())).thenReturn(tableGroup);

        // when
        final TableGroup createdTableGroup = tableGroupService.create(tableGroup);

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
            final TableGroup createdTableGroup = tableGroupService.create(new TableGroup());

            // then
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹이 가진 주문 테이블과 데이터베이스에서 가져온 주문 테이블 갯수는 같다.")
    @Test
    void createTableGroupSameOrderTableSizeFromDBExceptionTest() {
        assertThatThrownBy(() -> {
            when(orderTableDao.findAllByIdIn(any())).thenReturn(Lists.newArrayList(new OrderTable()));

            // when
            final TableGroup createdTableGroup = tableGroupService.create(tableGroup);

            // then
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 생성 시 주문 테이블은 비어있어야 한다.")
    @Test
    void createTableGroupEmptyOrderTablesFromDBExceptionTest() {
        assertThatThrownBy(() -> {
            when(orderTableDao.findAllByIdIn(any())).thenReturn(Lists.newArrayList(new OrderTable(), new OrderTable()));

            // when
            final TableGroup createdTableGroup = tableGroupService.create(tableGroup);

            // then
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹에 주문 테이블들을 등록한다.")
    @Test
    void createOrderTableInTableGroupTest() {
        when(orderTableDao.findAllByIdIn(any())).thenReturn(Lists.newArrayList(orderTable, orderTable2));
        when(tableGroupDao.save(any())).thenReturn(tableGroup);

        // when
        final TableGroup createdTableGroup = tableGroupService.create(tableGroup);

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
            when(orderTableDao.findAllByIdIn(any())).thenReturn(Lists.newArrayList(orderTable, orderTable2));
            when(tableGroupDao.save(any())).thenReturn(tableGroup);
            when(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(true);
            // given
            tableGroupService.create(tableGroup);

            // when
            tableGroupService.ungroup(1L);

        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블에 테이블 그룹을 해제한다.")
    @Test
    void ungroupTableGroupTest() {
        when(orderTableDao.findAllByIdIn(any())).thenReturn(Lists.newArrayList(orderTable, orderTable2));
        when(tableGroupDao.save(any())).thenReturn(tableGroup);
        // given
        tableGroupService.create(tableGroup);

        // when
        tableGroupService.ungroup(1L);

        // then
        assertThat(orderTable.getTableGroupId()).isEqualTo(null);
    }
}