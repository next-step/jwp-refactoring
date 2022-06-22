package kitchenpos.application;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;
import kitchenpos.application.fixture.OrderTableFixtureFactory;
import kitchenpos.application.fixture.TableGroupFixtureFactory;
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

    private OrderTable 주문_테이블1;
    private OrderTable 주문_테이블2;

    private TableGroup 단체;

    @BeforeEach
    void before() {
        주문_테이블1 = OrderTableFixtureFactory.createWithTableGroup(1L, null, true);
        주문_테이블2 = OrderTableFixtureFactory.createWithTableGroup(2L, null, true);
        단체 = TableGroupFixtureFactory.create(1L, Arrays.asList(주문_테이블1, 주문_테이블2));
    }

    @Test
    @DisplayName("주문 테이블이 비어 있으면 테이블 그룹으로 지정 할 수 없다.")
    void createFailWithEmptyTest() {

        //given
        TableGroup tableGroup = new TableGroup(1L, Collections.emptyList());

        //when & then
        assertThatThrownBy(
                () -> tableGroupService.create(tableGroup)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 2개보다 작으면 테이블 그룹으로 지정 할 수 없다.")
    void createFailWithUnder2Test() {

        //given
        TableGroup tableGroup = new TableGroup(1L, Arrays.asList(주문_테이블1));

        //when & then
        assertThatThrownBy(
                () -> tableGroupService.create(tableGroup)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블들이 시스템에 등록 되어 있지 않으면 테이블 그룹은 지정 할 수 없다.")
    void createFailWithOrderTableNotExistTest() {

        //given
        TableGroup tableGroup = new TableGroup(1L, Arrays.asList(주문_테이블1, 주문_테이블2));
        given(orderTableDao.findAllByIdIn(Arrays.asList(주문_테이블1.getId(), 주문_테이블2.getId()))).willReturn(
                Arrays.asList(주문_테이블1));

        //when & then
        assertThatThrownBy(
                () -> tableGroupService.create(tableGroup)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 빈테이블이 아니면 테이블 그룹을 지정 할 수 없다.")
    void createFailWithEmptyTableTest() {
        //given
        OrderTable orderTable1 = new OrderTable(1L, false);
        OrderTable orderTable2 = new OrderTable(2L, false);
        given(orderTableDao.findAllByIdIn(Arrays.asList(orderTable1.getId(), orderTable2.getId()))).willReturn(
                Arrays.asList(orderTable2, orderTable2));

        //when & then
        assertThatThrownBy(
                () -> tableGroupService.create(단체)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미 테이블 그룹에 속해 있으면 테이블 그룹을 지정 할 수 없다.")
    void createFailWithTableGroupTest() {
        //given
        TableGroup tableGroup = new TableGroup(1L);
        OrderTable orderTable1 = new OrderTable(1L, tableGroup.getId(), true);
        OrderTable orderTable2 = new OrderTable(2L, tableGroup.getId(), true);
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

        given(orderTableDao.findAllByIdIn(Arrays.asList(orderTable1.getId(), orderTable2.getId()))).willReturn(
                Arrays.asList(orderTable1, orderTable2));

        //when & then
        assertThatThrownBy(
                () -> tableGroupService.create(tableGroup)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹을 지정 할 수 있다.")
    void createTest() {
        //given
        given(orderTableDao.findAllByIdIn(Arrays.asList(주문_테이블1.getId(), 주문_테이블2.getId()))).willReturn(
                Arrays.asList(주문_테이블1, 주문_테이블1));
        given(tableGroupDao.save(any(TableGroup.class))).willReturn(단체);
        given(orderTableDao.save(any(OrderTable.class))).willReturn(any(OrderTable.class));

        //when
        TableGroup tableGroup = tableGroupService.create(단체);
        //then
        assertThat(tableGroup).isEqualTo(단체);
    }


    @Test
    @DisplayName("주문 상태가 조리중(COOKING), 식사중(MEAL)인 경우에는 해제 할 수 없다.")
    void ungroupFailWithStatusTest() {
        //given
        given(orderTableDao.findAllByTableGroupId(단체.getId())).willReturn(Arrays.asList(주문_테이블1, 주문_테이블2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        //when & then
        assertThatThrownBy(
                () -> tableGroupService.ungroup(단체.getId())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("지정된 테이블 그룹을 해제 할 수 있다.")
    void ungroupTest() {
        //given
        given(orderTableDao.findAllByTableGroupId(단체.getId())).willReturn(Arrays.asList(주문_테이블1, 주문_테이블2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(false);

        //when
        tableGroupService.ungroup(단체.getId());

        //then
        verify(orderTableDao, times(2)).save(any(OrderTable.class));
    }
}
