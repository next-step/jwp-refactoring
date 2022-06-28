package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;

import static kitchenpos.factory.TableGroupFixture.테이블그룹_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @InjectMocks
    TableGroupService tableGroupService;

    @Mock
    OrderDao orderDao;

    @Mock
    OrderTableDao orderTableDao;

    @Mock
    TableGroupDao tableGroupDao;

    TableGroup 주문테이블그룹;

    OrderTable 주문테이블1;
    OrderTable 주문테이블2;

    @Test
    @DisplayName("주문테이블그룹을 생성한다.(Happy Path)")
    void create() {
        //given
        주문테이블1 = 테이블그룹_생성(1L);
        주문테이블2 = 테이블그룹_생성(2L);
        주문테이블그룹 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(주문테이블1, 주문테이블2));
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(주문테이블1, 주문테이블2));
        given(tableGroupDao.save(any(TableGroup.class))).willReturn(주문테이블그룹);

        //when
        TableGroup savedTableGroup = tableGroupService.create(주문테이블그룹);

        //then
        assertThat(savedTableGroup).isNotNull()
                .satisfies(tableGroup -> {
                            tableGroup.getId().equals(주문테이블그룹.getId());
                            tableGroup.getOrderTables().containsAll(Arrays.asList(주문테이블1, 주문테이블2));
                        }
                );
    }

    @Test
    @DisplayName("테이블 그룹에 2개 이상의 테이블이 없는 경우 테이블 그룹 생성 불가")
    void createInvalidOrderTables() {
        //given
        주문테이블1 = 테이블그룹_생성(1L);
        주문테이블2 = 테이블그룹_생성(2L);
        주문테이블그룹 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(주문테이블1));

        //then
        assertThatThrownBy(() -> {
            tableGroupService.create(주문테이블그룹);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹에 유효하지 않은 테이블이 존재할 경우 그룹 생성 불가")
    void createInvalidOrderTable() {
        //given
        주문테이블1 = 테이블그룹_생성(1L);
        주문테이블2 = 테이블그룹_생성(2L);
        OrderTable 유효하지않은테이블 = new OrderTable();
        주문테이블그룹 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(주문테이블1, 주문테이블2, 유효하지않은테이블));
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(주문테이블1, 주문테이블2));

        //then
        assertThatThrownBy(() -> {
            tableGroupService.create(주문테이블그룹);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹내 테이블이 다른 테이블 그룹에 포함되어 있는 경우 그룹 생성 불가")
    void createInvalidAssignedTableGroup() {
        //given
        주문테이블1 = 테이블그룹_생성(1L);
        주문테이블2 = 테이블그룹_생성(2L);
        주문테이블1.setTableGroupId(2L);
        주문테이블2.setTableGroupId(2L);
        주문테이블그룹 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(주문테이블1, 주문테이블2));
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(주문테이블1, 주문테이블2));

        //then
        assertThatThrownBy(() -> {
            tableGroupService.create(주문테이블그룹);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹내 테이블이 중복되어 있을 경우 그룹 생성 불가")
    void createDuplicateTable() {
        //given
        주문테이블1 = 테이블그룹_생성(1L);
        주문테이블2 = 테이블그룹_생성(2L);
        주문테이블1.setTableGroupId(1L);
        주문테이블2.setTableGroupId(1L);
        주문테이블그룹 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(주문테이블1, 주문테이블1, 주문테이블2));
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(주문테이블1, 주문테이블2));

        //then
        assertThatThrownBy(() -> {
            tableGroupService.create(주문테이블그룹);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문테이블 그룹에 포함된 주물테이블들 그룹 해제(Happy Path)")
    void ungroup() {
        //given
        주문테이블1 = 테이블그룹_생성(1L);
        주문테이블2 = 테이블그룹_생성(2L);
        주문테이블그룹 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(주문테이블1, 주문테이블2));
        주문테이블1.setTableGroupId(주문테이블그룹.getId());
        주문테이블2.setTableGroupId(주문테이블그룹.getId());
        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(Arrays.asList(주문테이블1, 주문테이블2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(false);
        given(orderTableDao.save(주문테이블1)).willReturn(주문테이블1);
        given(orderTableDao.save(주문테이블2)).willReturn(주문테이블2);

        //when
        tableGroupService.ungroup(주문테이블그룹.getId());

        //then
        assertThat(주문테이블1.getTableGroupId()).isNull();
        assertThat(주문테이블2.getTableGroupId()).isNull();
    }

    @Test
    @DisplayName("주문테이블 그룹에 포함된 주물테이블들이 조리중/식사중일 경우 그룹 해제 불가")
    void ungroupInvalidOrderStatus() {
        //given
        주문테이블1 = 테이블그룹_생성(1L);
        주문테이블2 = 테이블그룹_생성(2L);
        주문테이블그룹 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(주문테이블1, 주문테이블2));
        주문테이블1.setTableGroupId(주문테이블그룹.getId());
        주문테이블2.setTableGroupId(주문테이블그룹.getId());
        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(Arrays.asList(주문테이블1, 주문테이블2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        //then
        assertThatThrownBy(() -> {
            tableGroupService.ungroup(주문테이블그룹.getId());
        }).isInstanceOf(IllegalArgumentException.class);
    }
}