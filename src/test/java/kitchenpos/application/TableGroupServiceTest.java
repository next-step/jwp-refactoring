package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.graalvm.compiler.core.common.type.ArithmeticOpTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

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
        tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
    }

    @Test
    public void 테이블그룹생성_성공() {
        //given
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setEmpty(true);
        orderTable1.setTableGroupId(null);

        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setEmpty(true);
        orderTable2.setTableGroupId(null);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setOrderTables(asList(orderTable1, orderTable2));

        given(orderTableDao.findAllByIdIn(any())).willReturn(asList(orderTable1,orderTable2));

        given(tableGroupDao.save(tableGroup)).willReturn(tableGroup);
        given(orderTableDao.save(any())).willReturn(new OrderTable());

        //when
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        //then
        assertThat(savedTableGroup.getOrderTables().size()).isEqualTo(2);
    }

    @Test
    public void 테이블그룹생성_예외_그룹화할_주문테이블_없는경우() {
        //given

        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);

        //when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void 테이블그룹생성_예외_그룹화할_주문테이블이_2개보다작은경우() {
        //given
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setEmpty(true);
        orderTable1.setTableGroupId(null);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setOrderTables(asList(orderTable1));

        //when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void 테이블그룹생성_예외_그룹화할_주문테이블이_DB에_없는경우() {
        //given
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setEmpty(true);
        orderTable1.setTableGroupId(null);

        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setEmpty(true);
        orderTable2.setTableGroupId(null);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setOrderTables(asList(orderTable1, orderTable2));

        given(orderTableDao.findAllByIdIn(any())).willReturn(asList(new OrderTable()));

        //when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void 테이블그룹생성_예외_그룹화할_주문테이블의_그룹이존재하는경우() {
        //given
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setEmpty(true);
        orderTable1.setTableGroupId(1L);

        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setEmpty(true);
        orderTable2.setTableGroupId(null);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setOrderTables(asList(orderTable1, orderTable2));

        given(orderTableDao.findAllByIdIn(any())).willReturn(asList(orderTable1, orderTable2));

        //when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void 테이블그룹생성_예외_그룹화할_주문테이블이_주문을등록할수없는_테이블인_경우() {
        //given
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setEmpty(false);
        orderTable1.setTableGroupId(null);

        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setEmpty(true);
        orderTable2.setTableGroupId(null);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setOrderTables(asList(orderTable1, orderTable2));

        given(orderTableDao.findAllByIdIn(any())).willReturn(asList(orderTable1, orderTable2));

        //when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void 테이블그룹해제_성공() {
        //given
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setTableGroupId(1L);

        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setTableGroupId(1L);

        given(orderTableDao.findAllByTableGroupId(any())).willReturn(asList(orderTable1,orderTable2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(false);
        given(orderTableDao.save(any())).willReturn(new OrderTable());

        //when
        tableGroupService.ungroup(1L);

        //then
        assertThat(orderTable1.getTableGroupId()).isNull();
        assertThat(orderTable2.getTableGroupId()).isNull();
    }

    @Test
    public void 테이블그룹해제_예외_조리또는식사중인_테이블을해제하는경우() {
        //given
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        //when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(1L)).isInstanceOf(IllegalArgumentException.class);
    }
}