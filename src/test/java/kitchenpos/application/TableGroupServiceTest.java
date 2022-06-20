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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.fixture.OrderTableFixture.주문테이블_데이터_생성;
import static kitchenpos.fixture.TableGroupFixture.*;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    private TableGroupService tableGroupService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
    }

    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    void create() {
        //given
        OrderTable table1 = 주문테이블_데이터_생성(1L, null, 4, true);
        OrderTable table2 = 주문테이블_데이터_생성(2L, null, 3, true);
        List<OrderTable> orderTables = Arrays.asList(table1, table2);
        TableGroup request = 단체_지정_데이터_생성(orderTables);

        given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);
        Long tableGroupId = 1L;
        LocalDateTime createdDate = LocalDateTime.now();
        given(tableGroupDao.save(any())).willReturn(단체_데이터_생성(tableGroupId, createdDate, orderTables));
        OrderTable groupingTable1 = 주문테이블_데이터_생성(1L, tableGroupId, 4, true);
        OrderTable groupingTable2 = 주문테이블_데이터_생성(2L, tableGroupId, 3, true);
        given(orderTableDao.save(any())).willReturn(groupingTable1, groupingTable2);

        //when
        TableGroup tableGroup = tableGroupService.create(request);

        //then
        단체_데이터_확인(tableGroup, tableGroupId, createdDate);
    }

    @DisplayName("주문 테이블 비었으면 생성할 수 없다.")
    @Test
    void create_fail_emptyTables() {
        //given
        TableGroup request = 단체_지정_데이터_생성(Collections.emptyList());
        //when //then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(request));
    }

    @DisplayName("주문 테이블 하나만 있으면 생성할 수 없다.")
    @Test
    void create_fail_onlyOneTable() {
        //given
        OrderTable table = 주문테이블_데이터_생성(1L, null, 4, true);
        TableGroup request = 단체_지정_데이터_생성(Arrays.asList(table));

        //when //then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(request));
    }

    @DisplayName("조회되지 않는 주문 테이블이 있으면 생성할 수 없다.")
    @Test
    void create_fail_notExistsTable() {
        //given
        OrderTable table1 = 주문테이블_데이터_생성(1L, null, 4, true);
        OrderTable table2 = 주문테이블_데이터_생성(2L, null, 3, true);
        List<OrderTable> orderTables = Arrays.asList(table1, table2);
        TableGroup request = 단체_지정_데이터_생성(orderTables);

        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(table1));

        //when //then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(request));
    }

    @DisplayName("조회된 테이블 중, 빈테이블이 있으면 생성할 수 없다.")
    @Test
    void create_fail_emptyTable() {
        //given
        OrderTable table1 = 주문테이블_데이터_생성(1L, null, 4, false);
        OrderTable table2 = 주문테이블_데이터_생성(2L, null, 3, true);
        List<OrderTable> orderTables = Arrays.asList(table1, table2);
        TableGroup request = 단체_지정_데이터_생성(orderTables);

        given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);

        //when //then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(request));
    }

    @Test
    void ungroup() {
        //given
        Long tableGroupId = 1L;
        OrderTable table1 = 주문테이블_데이터_생성(1L, tableGroupId, 4, true);
        OrderTable table2 = 주문테이블_데이터_생성(2L, tableGroupId, 4, true);
        given(orderTableDao.findAllByTableGroupId(tableGroupId)).willReturn(Arrays.asList(table1, table2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(false);

        //when //then
        tableGroupService.ungroup(tableGroupId);
    }

    @DisplayName("주문상태가 조리나 식사이면 해제할 수 없다.")
    @Test
    void ungroup_fail_invalidOrderStatus() {
        //given
        Long tableGroupId = 1L;
        OrderTable table1 = 주문테이블_데이터_생성(1L, tableGroupId, 4, true);
        OrderTable table2 = 주문테이블_데이터_생성(2L, tableGroupId, 4, true);
        given(orderTableDao.findAllByTableGroupId(tableGroupId)).willReturn(Arrays.asList(table1, table2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        //when //then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.ungroup(tableGroupId));
    }
}