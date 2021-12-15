package kitchenpos.application;

import static kitchenpos.application.TableServiceTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(
            orderDao, orderTableDao, tableGroupDao);
    }

    @DisplayName("주문 테이블 그룹을 등록할 수 있다.")
    @Test
    void createTableGroup() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
            주문_테이블_생성(1L, 0, true),
            주문_테이블_생성(2L, 0, true));
        TableGroup tableGroup = 주문_테이블_그룹_생성(orderTables);

        given(orderTableDao.findAllByIdIn(any()))
            .willReturn(orderTables);
        given(tableGroupDao.save(any())).willReturn(tableGroup);
        given(orderTableDao.save(any())).willReturn(null);

        // when
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // then
        assertThat(savedTableGroup.getOrderTables())
            .extracting("empty")
            .containsExactly(false, false);
    }

    @DisplayName("주문 테이블 그룹은 2 테이블 이상이어야 등록할 수 있다.")
    @Test
    void createTableGroupMinSize() {
        // given
        List<OrderTable> orderTables = Collections.singletonList(
            주문_테이블_생성(2L, 0, true));
        TableGroup tableGroup = 주문_테이블_그룹_생성(orderTables);

        // when && then
        assertThrows(IllegalArgumentException.class, () ->
            tableGroupService.create(tableGroup));
        verify(orderTableDao, times(0)).findAllByIdIn(any());
    }

    @DisplayName("조회 가능한 주문 테이블만 주문 테이블 그룹에 등록할 수 있다.")
    @Test
    void createTableGroupNotFoundTable() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
            주문_테이블_생성(1L, 0, true),
            주문_테이블_생성(2L, 0, true));
        TableGroup tableGroup = 주문_테이블_그룹_생성(orderTables);

        given(orderTableDao.findAllByIdIn(any())).willReturn(Collections.emptyList());

        // when && then
        assertThrows(IllegalArgumentException.class, () ->
            tableGroupService.create(tableGroup));
        verify(orderTableDao).findAllByIdIn(any());
        verify(tableGroupDao, times(0)).save(any());
    }

    @DisplayName("빈 주문 테이블만 주문 테이블 그룹에 등록할 수 있다.")
    @Test
    void createTableGroupNotEmptyTable() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
            주문_테이블_생성(1L, 0, true),
            주문_테이블_생성(2L, 0, false));
        TableGroup tableGroup = 주문_테이블_그룹_생성(orderTables);

        given(orderTableDao.findAllByIdIn(any())).willReturn(orderTables);

        // when && then
        assertThrows(IllegalArgumentException.class, () ->
            tableGroupService.create(tableGroup));
        verify(orderTableDao).findAllByIdIn(any());
        verify(tableGroupDao, times(0)).save(any());
    }

    @DisplayName("주문 테이블 그룹에 속해있지 않은 주문 테이블만 등록 가능하다.")
    @Test
    void createTableGroupExistTableGroup() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
            주문_테이블_생성(1L, 1L, 0, true),
            주문_테이블_생성(2L, 0, true));
        TableGroup tableGroup = 주문_테이블_그룹_생성(orderTables);

        given(orderTableDao.findAllByIdIn(any())).willReturn(orderTables);

        // when && then
        assertThrows(IllegalArgumentException.class, () ->
            tableGroupService.create(tableGroup));
        verify(orderTableDao).findAllByIdIn(any());
        verify(tableGroupDao, times(0)).save(any());
    }

    @DisplayName("주문 테이블 그룹에서 주문 테이블을 삭제할 수 있다.")
    @Test
    void ungroup() {
        // given
        Long tableGroupId = 1L;
        List<OrderTable> orderTables = Arrays.asList(
            주문_테이블_생성(1L, tableGroupId, 0, true),
            주문_테이블_생성(2L, tableGroupId, 0, true));

        given(orderTableDao.findAllByTableGroupId(any()))
            .willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
            .willReturn(false);

        // when
        tableGroupService.ungroup(tableGroupId);

        // then
        assertThat(orderTables).extracting("tableGroupId")
            .containsExactly(null, null);
    }

    private TableGroup 주문_테이블_그룹_생성(List<OrderTable> orderTables) {
        return new TableGroup(orderTables);
    }
}
