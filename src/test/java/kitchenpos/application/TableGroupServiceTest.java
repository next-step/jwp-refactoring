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

import java.util.Arrays;
import java.util.List;

import static kitchenpos.application.ServiceTestFactory.테이블그룹생성;
import static kitchenpos.application.ServiceTestFactory.테이블생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@DisplayName("테이블그룹 서비스 테스트")
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
    private OrderTable table1;
    private OrderTable table2;
    private List<OrderTable> orderTables;
    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        table1 = 테이블생성(1L, null, 3, true);
        table2 = 테이블생성(2L, null, 3, true);
        orderTables = Arrays.asList(table1, table2);
        tableGroup = 테이블그룹생성(1L, orderTables);
    }

    @Test
    void 테이블그룹_생성() {
        given(orderTableDao.findAllByIdIn(Arrays.asList(table1.getId(), table2.getId()))).willReturn(orderTables);
        given(tableGroupDao.save(any(TableGroup.class))).willReturn(tableGroup);

        TableGroup result = tableGroupService.create(tableGroup);

        assertThat(result.getOrderTables()).hasSize(2);
    }

    @Test
    void 테이블그룹_생성_실패_주문테이블이_2개미만() {
        TableGroup 테이블그룹_주문테이블1개 = 테이블그룹생성(2L, Arrays.asList(table1));

        assertThatThrownBy(() -> tableGroupService.create(테이블그룹_주문테이블1개))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹_생성_실패_등록되지않은_주문테이블() {
        given(orderTableDao.findAllByIdIn(Arrays.asList(table1.getId(), table2.getId())))
                .willReturn(Arrays.asList(table1));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹_생성_실패_빈_주문테이블() {
        OrderTable 빈테이블1 = 테이블생성(1L, null, 3, false);
        OrderTable 빈테이블2 = 테이블생성(1L, null, 3, false);
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(빈테이블1, 빈테이블2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹_생성_실패_테이블그룹아이디가_설정되어있음() {
        OrderTable 빈테이블1 = 테이블생성(1L, 1L, 3, true);
        OrderTable 빈테이블2 = 테이블생성(1L, 1L, 3, true);
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(빈테이블1, 빈테이블2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹_삭제() {
        OrderTable 그룹있는_테이블1 = 테이블생성(1L, 1L, 3, true);
        OrderTable 그룹있는_테이블2 = 테이블생성(1L, 1L, 3, true);
        given(orderTableDao.findAllByTableGroupId(1L)).willReturn(Arrays.asList(그룹있는_테이블1, 그룹있는_테이블2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(Boolean.FALSE);

        tableGroupService.ungroup(1L);

        assertThat(그룹있는_테이블1.getTableGroupId()).isNull();
        assertThat(그룹있는_테이블2.getTableGroupId()).isNull();
    }

    @Test
    void 테이블그룹_삭제_실패_주문상태가_요리중이거나_식사중() {
        given(orderTableDao.findAllByTableGroupId(1L)).willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(Boolean.TRUE);

        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
