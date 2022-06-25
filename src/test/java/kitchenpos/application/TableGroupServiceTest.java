package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Arrays;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("단체 지정 관련")
@SpringBootTest
class TableGroupServiceTest {
    @Autowired
    TableGroupService tableGroupService;
    @MockBean
    OrderDao orderDao;
    @MockBean
    OrderTableDao orderTableDao;
    @MockBean
    TableGroupDao tableGroupDao;

    Long orderTableId1 = 1L;
    Long orderTableId2 = 2L;
    OrderTable orderTable1 = new OrderTable(orderTableId1, null, 1, true);
    OrderTable orderTable2 = new OrderTable(orderTableId2, null, 1, true);

    @DisplayName("여러 주문 테이블을 단체 지정할 수 있다")
    @Test
    void create() {
        // given
        TableGroup tableGroup = new TableGroup(null, null, Arrays.asList(orderTable1, orderTable2));
        when(orderTableDao.findAllByIdIn(Arrays.asList(orderTableId1, orderTableId2))).thenReturn(Arrays.asList(orderTable1, orderTable2));
        when(tableGroupDao.save(tableGroup)).thenReturn(new TableGroup(1L, LocalDateTime.now(), null));

        // when
        TableGroup actual = tableGroupService.create(tableGroup);

        // then
        verify(orderTableDao).save(orderTable1);
        verify(orderTableDao).save(orderTable2);
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isNotNull();
            softAssertions.assertThat(actual.getCreatedDate()).isNotNull();
            softAssertions.assertThat(actual.getOrderTables()).hasSize(2);
            softAssertions.assertThat(actual.getOrderTables()).allSatisfy(orderTable -> {
                softAssertions.assertThat(orderTable.getTableGroupId()).isEqualTo(actual.getId());
                softAssertions.assertThat(orderTable.isEmpty()).isFalse();
            });
        });
    }

    @DisplayName("주문 테이블은 2개 이상이어야 한다")
    @Test
    void orderTable_size_2_over() {
        // given
        TableGroup tableGroup = new TableGroup(null, null, singletonList(orderTable1));
        when(orderTableDao.findAllByIdIn(singletonList(orderTableId1))).thenReturn(singletonList(orderTable1));
        when(tableGroupDao.save(tableGroup)).thenReturn(new TableGroup(1L, LocalDateTime.now(), null));

        // when
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("없는 주문 테이블은 단체 지정할 수 없다")
    @Test
    void orderTable_is_exists() {
        // given
        Long notExistsOrderTableId = 1000L;
        OrderTable notExistsOrderTable = new OrderTable(notExistsOrderTableId, null, 1, true);
        TableGroup tableGroup = new TableGroup(null, null, Arrays.asList(orderTable1, notExistsOrderTable));
        when(orderTableDao.findAllByIdIn(Arrays.asList(orderTableId1, notExistsOrderTableId))).thenReturn(singletonList(orderTable1));

        // when
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블이 아니면 단체 지정할 수 없다")
    @Test
    void not_empty_table() {
        // given
        Long notEmptyTableId = 1000L;
        OrderTable notEmptyTable = new OrderTable(notEmptyTableId, null, 1, false);
        TableGroup tableGroup = new TableGroup(null, null, Arrays.asList(orderTable1, notEmptyTable));
        when(orderTableDao.findAllByIdIn(Arrays.asList(orderTableId1, notEmptyTableId))).thenReturn(Arrays.asList(orderTable1, notEmptyTable));

        // when
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 단체에 지정된 테이블은 단체 지정할 수 없다")
    @Test
    void grouped_table() {
        // given
        Long groupedTableId = 1000L;
        OrderTable groupedTable = new OrderTable(groupedTableId, 1L, 1, true);
        TableGroup tableGroup = new TableGroup(null, null, Arrays.asList(orderTable1, groupedTable));
        when(orderTableDao.findAllByIdIn(Arrays.asList(orderTableId1, groupedTableId))).thenReturn(Arrays.asList(orderTable1, groupedTable));

        // when
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해제할 수 있다")
    @Test
    void ungroup() {
        // given
        Long tableGroupId = 1L;
        Long groupedTableId1 = 1L;
        Long groupedTableId2 = 2L;
        OrderTable groupedTable1 = new OrderTable(groupedTableId1, tableGroupId, 1, false);
        OrderTable groupedTable2 = new OrderTable(groupedTableId2, tableGroupId, 1, false);
        when(orderTableDao.findAllByTableGroupId(tableGroupId)).thenReturn(Arrays.asList(groupedTable1, groupedTable2));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(groupedTableId1, groupedTableId2), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);

        // when
        tableGroupService.ungroup(tableGroupId);

        // then
        verify(orderTableDao).save(groupedTable1);
        verify(orderTableDao).save(groupedTable2);
    }

    @DisplayName("단체에 속한 주문 테이블에 등록된 주문의 상태가 조리, 식사인 경우 해제할 수 없다")
    @Test
    void orderStatus_is_not_in_cooking_meal() {
        // given
        Long tableGroupId = 1L;
        Long groupedTableId1 = 1L;
        Long groupedTableId2 = 2L;
        OrderTable groupedTable1 = new OrderTable(groupedTableId1, tableGroupId, 1, false);
        OrderTable groupedTable2 = new OrderTable(groupedTableId2, tableGroupId, 1, false);
        when(orderTableDao.findAllByTableGroupId(tableGroupId)).thenReturn(Arrays.asList(groupedTable1, groupedTable2));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(groupedTableId1, groupedTableId2), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);

        // when
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
