package kitchenpos.application;

import static kitchenpos.domain.OrderTableFixture.*;
import static kitchenpos.domain.TableGroupFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@DisplayName("단체 지정 서비스 테스트")
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

    @DisplayName("단체 지정 등록 API - 주문 테이블 없음")
    @Test
    void create_empty_orderTables() {
        // given
        TableGroup tableGroup = tableGroupParam(Collections.emptyList());

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 등록 API - 주문 테이블 2개 미만")
    @Test
    void create_orderTables_lessThan2() {
        // given
        TableGroup tableGroup = tableGroupParam(Collections.singletonList(orderTableParam(1L)));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 등록 API - 등록되어있지 않는 주문 테이블 존재")
    @Test
    void create_orderTables_notSaved() {
        // given
        Long orderTableId1 = 1L;
        Long orderTableId2 = 2L;
        TableGroup tableGroup = tableGroupParam(
            Arrays.asList(orderTableParam(orderTableId1), orderTableParam(orderTableId2)));
        given(orderTableDao.findAllByIdIn(Arrays.asList(orderTableId1, orderTableId2)))
            .willReturn(Collections.singletonList(savedOrderTable(orderTableId1, true)));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 등록 API - 비어있지 않은 주문 테이블 존재")
    @Test
    void create_orderTable_not_empty() {
        // given
        Long orderTableId1 = 1L;
        Long orderTableId2 = 2L;
        TableGroup tableGroup = tableGroupParam(
            Arrays.asList(orderTableParam(orderTableId1), orderTableParam(orderTableId2)));
        given(orderTableDao.findAllByIdIn(Arrays.asList(orderTableId1, orderTableId2)))
            .willReturn(Arrays.asList(
                savedOrderTable(orderTableId1, true),
                savedOrderTable(orderTableId2, false)
            ));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 등록 API - 다른 단체 지정에 연결되어있는 주문 테이블 존재")
    @Test
    void create_orderTable_tableGroupId_exists() {
        // given
        Long orderTableId1 = 1L;
        Long orderTableId2 = 2L;
        TableGroup tableGroup = tableGroupParam(
            Arrays.asList(orderTableParam(orderTableId1), orderTableParam(orderTableId2)));
        given(orderTableDao.findAllByIdIn(Arrays.asList(orderTableId1, orderTableId2)))
            .willReturn(Arrays.asList(
                savedOrderTable(orderTableId1, true),
                savedOrderTable(orderTableId2, 1L, true)
            ));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 등록 API")
    @Test
    void create() {
        // given
        Long tableGroupId = 1L;
        Long orderTableId1 = 1L;
        Long orderTableId2 = 2L;
        OrderTable savedOrderTable1 = savedOrderTable(orderTableId1, true);
        OrderTable savedOrderTable2 = savedOrderTable(orderTableId2, true);
        TableGroup tableGroup = tableGroupParam(
            Arrays.asList(orderTableParam(orderTableId1), orderTableParam(orderTableId2)));
        given(orderTableDao.findAllByIdIn(Arrays.asList(orderTableId1, orderTableId2)))
            .willReturn(Arrays.asList(savedOrderTable1, savedOrderTable2));
        given(tableGroupDao.save(tableGroup)).willReturn(
            savedTableGroup(tableGroupId, Arrays.asList(savedOrderTable1, savedOrderTable2)));
        given(orderTableDao.save(savedOrderTable1)).willReturn(savedOrderTable1);
        given(orderTableDao.save(savedOrderTable2)).willReturn(savedOrderTable2);

        // when
        TableGroup actual = tableGroupService.create(tableGroup);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getOrderTables()).hasSize(2),
            () -> assertThat(actual.getOrderTables()).containsExactly(savedOrderTable1, savedOrderTable2),
            () -> assertThat(savedOrderTable1.isEmpty()).isFalse(),
            () -> assertThat(savedOrderTable1.getTableGroupId()).isEqualTo(1L),
            () -> assertThat(savedOrderTable2.isEmpty()).isFalse(),
            () -> assertThat(savedOrderTable2.getTableGroupId()).isEqualTo(1L)
        );
    }

    @DisplayName("단체 지정 해제 API - 주문 상태 조리중 또는 식사중")
    @Test
    void upgroup_cooking_or_meal() {
        // given
        long tableGroupId = 1L;
        OrderTable orderTable1 = savedOrderTable(1L, tableGroupId, false);
        OrderTable orderTable2 = savedOrderTable(2L, tableGroupId, false);
        given(orderTableDao.findAllByTableGroupId(tableGroupId)).willReturn(Arrays.asList(orderTable1, orderTable2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), anyList())).willReturn(true);

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 해제 API")
    @Test
    void ungroup() {
        // given
        long tableGroupId = 1L;
        OrderTable orderTable1 = savedOrderTable(1L, tableGroupId, false);
        OrderTable orderTable2 = savedOrderTable(2L, tableGroupId, false);
        given(orderTableDao.findAllByTableGroupId(tableGroupId)).willReturn(Arrays.asList(orderTable1, orderTable2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), anyList())).willReturn(false);
        given(orderTableDao.save(orderTable1)).willReturn(orderTable1);
        given(orderTableDao.save(orderTable2)).willReturn(orderTable2);

        // when
        tableGroupService.ungroup(tableGroupId);

        // then
        assertAll(
            () -> assertThat(orderTable1.getTableGroupId()).isNull(),
            () -> assertThat(orderTable2.getTableGroupId()).isNull()
        );
    }
}
