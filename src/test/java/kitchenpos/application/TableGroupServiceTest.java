package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @Test
    @DisplayName("단체를 지정한다.")
    void create() {
        // given
        OrderTable orderTable1 = new OrderTable();
        OrderTable orderTable2 = new OrderTable();
        orderTable1.setEmpty(true);
        orderTable2.setEmpty(true);

        List<OrderTable> orderTables = new ArrayList<>(Arrays.asList(orderTable1, orderTable2));
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);

        given(orderTableDao.findAllByIdIn(any())).willReturn(orderTables);
        given(tableGroupDao.save(any())).willReturn(tableGroup);

        // when
        TableGroup result = tableGroupService.create(tableGroup);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getCreatedDate()).isNotNull();
    }

    @Test
    @DisplayName("주문 테이블이 1개 이하이면 단체 지정이 실패한다.")
    void create_order_table_is_empty_or_one() {
        // given
        List<OrderTable> orderTables = new ArrayList<>();
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);

        // given
        orderTables.add(new OrderTable());

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정할 주문 테이블들이 존재하는 주문테이블이 아니면 단체 지정에 실패한다.")
    void create_not_exist_order_tables() {
        // given
        OrderTable orderTable1 = new OrderTable();
        OrderTable orderTable2 = new OrderTable();
        orderTable1.setEmpty(true);
        orderTable2.setEmpty(true);

        List<OrderTable> orderTables = new ArrayList<>(Arrays.asList(orderTable1, orderTable2));
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);

        given(orderTableDao.findAllByIdIn(any())).willReturn(new ArrayList<>());

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블들이 빈 테이블이 아니거나 이미 단체로 지정되어 있으면 단체 지정에 실패한다.")
    void create_not_empty_or_already_table_group() {
        // given
        OrderTable orderTable1 = new OrderTable();
        OrderTable orderTable2 = new OrderTable();
        orderTable1.setEmpty(false);
        orderTable2.setEmpty(true);

        List<OrderTable> orderTables = new ArrayList<>(Arrays.asList(orderTable1, orderTable2));
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);

        given(orderTableDao.findAllByIdIn(any())).willReturn(orderTables);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);

        // given
        orderTable1.setEmpty(true);
        orderTable1.setTableGroupId(1L);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정을 해제한다.")
    void ungroup() {
        // given
        List<OrderTable> orderTables = new ArrayList<>(Arrays.asList(new OrderTable()));

        given(orderTableDao.findAllByTableGroupId(any())).willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(false);

        // when
        tableGroupService.ungroup(1L);

        // then
        assertThatNoException();
    }

    @Test
    @DisplayName("해제할 주문 테이블들 중에 조리 또는 식사 중인 주문 테이블이 존재하면 단체 지정 해제가 실패한다.")
    void ungroup_order_status_cooking_or_meal() {
        // given
        given(orderTableDao.findAllByTableGroupId(any())).willReturn(new ArrayList<>());
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
