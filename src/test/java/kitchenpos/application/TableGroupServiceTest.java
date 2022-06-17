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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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

    private TableGroup tableGroup;
    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        tableGroup = new TableGroup();
        orderTable1 = new OrderTable();
        orderTable1.setId(999L);
        orderTable2 = new OrderTable();
        orderTable2.setId(998L);
    }

    @Test
    @DisplayName("단체 지정을 할 수 있다.")
    void create() {
        //given
        orderTable1.setEmpty(true);
        orderTable1.setTableGroupId(null);
        orderTable2.setEmpty(true);
        orderTable2.setTableGroupId(null);
        given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable1, orderTable2));
        given(orderTableDao.save(orderTable1)).willReturn(orderTable1);
        given(orderTableDao.save(orderTable2)).willReturn(orderTable2);
        given(tableGroupDao.save(any())).willReturn(tableGroup);

        //when
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        //then
        assertThat(savedTableGroup).isInstanceOf(TableGroup.class);
        assertThat(savedTableGroup.getOrderTables()).isNotEmpty().containsExactly(orderTable1, orderTable2);
    }

    @Test
    @DisplayName("단체 지정에 속한 주문 테이블 수가 2 미만이면 실패한다.")
    void create_failed_1() {
        //given
        tableGroup.setOrderTables(Arrays.asList(orderTable1));

        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isExactlyInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("요청한 단체 지정의 주문 테이블 수와 실제 주문 테이블 갯수 차이가 나면 단체 지정에 실패한다.")
    void create_failed_2() {
        //given
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));
        given(orderTableDao.findAllByIdIn(any())).willReturn(Collections.emptyList());

        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isExactlyInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정 내 속한 주문 테이블 중 이미 단체 지정이 되어있거나, 빈 테이블이 아닌 주문 테이블이 있으면 단체 지정에 실패한다.")
    void create_failed_3() {
        //given
        OrderTable nonEmptyOrderTable = new OrderTable();
        nonEmptyOrderTable.setEmpty(false);

        OrderTable nonNullGroupIdOrderTable = new OrderTable();
        nonEmptyOrderTable.setTableGroupId(2L);

        given(orderTableDao.findAllByIdIn(any())).willReturn(
                Arrays.asList(nonEmptyOrderTable, nonNullGroupIdOrderTable));
        tableGroup.setOrderTables(Arrays.asList(nonEmptyOrderTable, nonNullGroupIdOrderTable));

        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isExactlyInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정을 해제할 수 있다.")
    void ungroup() {
        //given
        orderTable1.setTableGroupId(1L);
        orderTable2.setTableGroupId(1L);
        List<OrderTable> ungroupOrderTables = Arrays.asList(orderTable1, orderTable2);
        given(orderTableDao.findAllByTableGroupId(any())).willReturn(ungroupOrderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(false);
        given(orderTableDao.save(orderTable1)).willReturn(orderTable1);
        given(orderTableDao.save(orderTable2)).willReturn(orderTable2);

        //when
        tableGroupService.ungroup(0L);

        //then
        assertThat(orderTable1.getTableGroupId()).isNull();
        assertThat(orderTable2.getTableGroupId()).isNull();
    }

    @Test
    @DisplayName("단체 지정 내 주문 상태가 조리 혹은 식사 인 주문 테이블이 포함되어 있을 경우 해제할 수 없다.")
    void ungroup_failed_1() {
        //given
        given(orderTableDao.findAllByTableGroupId(any())).willReturn(Arrays.asList(orderTable1, orderTable2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);

        //then
        assertThatThrownBy(() -> tableGroupService.ungroup(0L)).isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
