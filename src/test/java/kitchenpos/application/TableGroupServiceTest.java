package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import kitchenpos.application.fixture.OrderTableFixtureFactory;
import kitchenpos.application.fixture.TableGroupFixtureFactory;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroup;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    private TableGroup 단체_1;
    private OrderTable 주문_1_테이블;
    private OrderTable 주문_2_테이블;
    private OrderTable 주문_테이블_10명;

    @BeforeEach
    void setUp() {
        주문_1_테이블 = OrderTableFixtureFactory.create(1L, true);
        주문_2_테이블 = OrderTableFixtureFactory.create(2L, true);
        주문_테이블_10명 = OrderTableFixtureFactory.createWithGuest(1L, false, 10);
        단체_1 = TableGroupFixtureFactory.create(1L);
        단체_1.setOrderTables(Lists.newArrayList(주문_1_테이블, 주문_2_테이블));
    }

    @DisplayName("단체를 지정할 수 있다.")
    @Test
    void create01() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Lists.newArrayList(주문_1_테이블, 주문_2_테이블));

        given(orderTableDao.findAllByIdIn(
                        Lists.newArrayList(주문_1_테이블.getId(), 주문_2_테이블.getId()))
        ).willReturn(Lists.newArrayList(주문_1_테이블, 주문_2_테이블));
        given(tableGroupDao.save(any(TableGroup.class))).willReturn(단체_1);

        // when
        TableGroup createTableGroup = tableGroupService.create(tableGroup);

        // then
        assertThat(createTableGroup).isEqualTo(단체_1);
    }

    @DisplayName("주문 테이블이 비어있으면 테이블을 단체로 지정할 수 없다.")
    @Test
    void create02() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Collections.emptyList());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("주문 테이블이 1개이면 테이블을 단체로 지정할 수 없다.")
    @Test
    void create03() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Lists.newArrayList(주문_1_테이블));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("단체에 속하는 주문 테이블이 존재하지 않으면 단체로 지정할 수 없다.")
    @Test
    void create04() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Lists.newArrayList(주문_1_테이블, 주문_2_테이블));

        given(orderTableDao.findAllByIdIn(
                Lists.newArrayList(주문_1_테이블.getId(), 주문_2_테이블.getId()))
        ).willReturn(Collections.emptyList());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableGroupService.create(tableGroup)
        );
    }

    @DisplayName("단체에 속하는 주문 테이블이 빈 테이블이 아니면 단체로 지정할 수 없다.")
    @Test
    void create05() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Lists.newArrayList(주문_테이블_10명));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableGroupService.create(tableGroup)
        );
    }

    @DisplayName("단체에 속하는 주문 테이블이 이미 테이블 그룹에 속해있으면 단체로 지정할 수 없다.")
    @Test
    void create06() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Lists.newArrayList(주문_1_테이블, 주문_2_테이블));

        주문_1_테이블.setTableGroupId(단체_1.getId());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableGroupService.create(tableGroup)
        );
    }

    @DisplayName("단체를 해제할 수 있다.")
    @Test
    void change01() {
        // given
        주문_1_테이블.setTableGroupId(단체_1.getId());
        주문_2_테이블.setTableGroupId(단체_1.getId());

        given(orderTableDao.findAllByTableGroupId(단체_1.getId())).willReturn(Lists.newArrayList(주문_1_테이블, 주문_2_테이블));

        // when
        tableGroupService.ungroup(단체_1.getId());

        // then
        assertAll(
                () -> verify(orderTableDao, times(2)).save(any(OrderTable.class)),
                () -> assertThat(주문_1_테이블.getTableGroupId()).isNull(),
                () -> assertThat(주문_2_테이블.getTableGroupId()).isNull()
        );
    }

    @DisplayName("주문 테이블의 주문 상태가 COOKING 이거나 MEAL 인 경우 단체를 해제할 수 없다.")
    @Test
    void change02() {
        // given
        주문_1_테이블.setTableGroupId(단체_1.getId());
        주문_2_테이블.setTableGroupId(단체_1.getId());

        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);
        given(orderTableDao.findAllByTableGroupId(단체_1.getId())).willReturn(
                Lists.newArrayList(주문_1_테이블, 주문_2_테이블)
        );

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableGroupService.ungroup(단체_1.getId())
        );
    }
}