package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

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

    @Test
    @DisplayName("단체 지정을 할 수 있다.")
    void create() {
        //given
        OrderTable emptyOrderTable = new OrderTable();
        emptyOrderTable.setTableGroupId(null);
        emptyOrderTable.setId(1L);
        emptyOrderTable.setEmpty(true);

        OrderTable nullGroupIdOrderTable = new OrderTable();
        nullGroupIdOrderTable.setTableGroupId(null);
        nullGroupIdOrderTable.setEmpty(true);
        nullGroupIdOrderTable.setId(2L);

        given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(emptyOrderTable, nullGroupIdOrderTable));
        given(orderTableDao.save(any())).willReturn(new OrderTable());
        given(tableGroupDao.save(any())).willReturn(new TableGroup());

        //when
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(emptyOrderTable, nullGroupIdOrderTable));
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        //then
        assertThat(savedTableGroup).isInstanceOf(TableGroup.class);
        assertThat(savedTableGroup.getOrderTables()).isNotEmpty();
    }

    @Test
    @DisplayName("단체 지정에 속한 주문 테이블 수가 2 미만이면 실패한다.")
    void create_failed_1() {
        //given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(new OrderTable()));

        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("요청한 단체 지정의 주문 테이블 수와 실제 저장된 주문 테이블 갯수 차이가 나면 단체 지정에 실패한다.")
    void create_failed_2() {
        //given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(new OrderTable()));

        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정 내 속한 주문 테이블 중 이미 단체 지정이 되어있거나, 빈 테이블이 아닌 주문 테이블이 있으면 단체 지정에 실패한다.")
    void create_failed_3() {
        //given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(new OrderTable(), new OrderTable()));

        OrderTable nonEmptyOrderTable = new OrderTable();
        nonEmptyOrderTable.setEmpty(false);

        OrderTable nonNullGroupIdOrderTable = new OrderTable();
        nonEmptyOrderTable.setTableGroupId(2L);

        given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(nonEmptyOrderTable, nonNullGroupIdOrderTable));

        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정을 해제할 수 있다.")
    void ungroup() {
        //given
        given(orderTableDao.findAllByTableGroupId(any())).willReturn(Collections.emptyList());
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(false);

        //then
        Assertions.assertDoesNotThrow(() -> tableGroupService.ungroup(0L));
    }

    @Test
    @DisplayName("단체 지정 내 주문 상태가 조리 혹은 식사 인 주문 테이블이 포함되어 있을 경우 해제할 수 없다.")
    void ungroup_failed_1() {
        //given
        given(orderTableDao.findAllByTableGroupId(any())).willReturn(Arrays.asList(new OrderTable(), new OrderTable()));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);

        //then
        assertThatThrownBy(() -> tableGroupService.ungroup(0L)).isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
