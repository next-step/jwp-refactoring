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
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@DisplayName("TableGroupService 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    private static final Long ANOTHER_TABLE_GROUP = 2L;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    private TableGroup 단체_테이블;
    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;
    private List<OrderTable> 주문_테이블_목록;

    @BeforeEach
    void setUp() {
        단체_테이블 = new TableGroup();
        단체_테이블.setId(1L);

        주문테이블1 = new OrderTable(1L, null, 4, true);
        주문테이블2 = new OrderTable(2L, null, 4, true);

        주문_테이블_목록 = Arrays.asList(주문테이블1, 주문테이블2);
        단체_테이블.setOrderTables(주문_테이블_목록);
    }

    @Test
    void 주문_테이블_목록이_2보다_작으면_테이블_그룹을_등록할_수_없음() {
        주문_테이블_목록 = Collections.emptyList();
        단체_테이블.setOrderTables(주문_테이블_목록);

        assertThatThrownBy(() -> {
            tableGroupService.create(단체_테이블);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 등록되어_있지_않은_주문_테이블이_포함되어_있으면_테이블_그룹을_등록할_수_없음() {
        given(orderTableDao.findAllByIdIn(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId())))
                .willReturn(Arrays.asList(주문테이블1));

        assertThatThrownBy(() -> {
            tableGroupService.create(단체_테이블);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 빈_테이블이_아닌_주문_테이블이_포함되어_있으면_테이블_그룹을_등록할_수_없음() {
        주문테이블1.setEmpty(false);

        given(orderTableDao.findAllByIdIn(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId())))
                .willReturn(Arrays.asList(주문테이블1, 주문테이블2));

        assertThatThrownBy(() -> {
            tableGroupService.create(단체_테이블);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 다른_테이블_그룹에_포함되어_있으면_테이블_그룹을_등록할_수_없음() {
        주문테이블1.setTableGroupId(ANOTHER_TABLE_GROUP);

        given(orderTableDao.findAllByIdIn(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId())))
                .willReturn(Arrays.asList(주문테이블1, 주문테이블2));

        assertThatThrownBy(() -> {
            tableGroupService.create(단체_테이블);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹을_등록() {
        given(orderTableDao.findAllByIdIn(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId())))
                .willReturn(Arrays.asList(주문테이블1, 주문테이블2));
        given(tableGroupDao.save(단체_테이블)).willReturn(단체_테이블);
        given(orderTableDao.save(주문테이블1)).willReturn(주문테이블1);
        given(orderTableDao.save(주문테이블2)).willReturn(주문테이블2);

        TableGroup savedTableGroup = tableGroupService.create(단체_테이블);

        assertThat(savedTableGroup).satisfies(tableGroup -> {
           assertEquals(단체_테이블.getId(), savedTableGroup.getId());
           assertEquals(단체_테이블.getOrderTables().size(), savedTableGroup.getOrderTables().size());
        });
    }

    @Test
    void 주문_테이블_중_조리중이거나_식사중인_테이블이_포함되어_있으면_테이블_그룹을_해제할_수_없음() {
        given(orderTableDao.findAllByTableGroupId(단체_테이블.getId())).willReturn(
                Arrays.asList(주문테이블1, 주문테이블2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                anyList(), anyList())).willReturn(true);

        assertThatThrownBy(() -> {
            tableGroupService.ungroup(단체_테이블.getId());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹을_해제() {
        given(orderTableDao.findAllByTableGroupId(단체_테이블.getId())).willReturn(
                Arrays.asList(주문테이블1, 주문테이블2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                anyList(), anyList())).willReturn(false);

        tableGroupService.ungroup(단체_테이블.getId());

        assertTrue(주문_테이블_목록.stream().allMatch(orderTable -> orderTable.getTableGroupId() == null));
    }
}
