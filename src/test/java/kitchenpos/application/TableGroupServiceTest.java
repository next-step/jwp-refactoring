package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    @DisplayName("테이블 그룹을 지정하고, 지정된 객체를 리턴한다")
    void create_table_group() {
        OrderTable orderTable1 = new OrderTable(1L, null, 3, true);
        OrderTable orderTable2 = new OrderTable(2L, null, 2, true);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        TableGroup givenTableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);
        when(orderTableDao.findAllByIdIn(anyList()))
                .thenReturn(orderTables);
        when(tableGroupDao.save(any(TableGroup.class)))
                .thenReturn(givenTableGroup);

        TableGroup actual = tableGroupService.create(givenTableGroup);

        assertThat(actual).isEqualTo(givenTableGroup);
    }

    @Test
    @DisplayName("주문 테이블 없이 테이블 그룹을 지정하면 예외를 던진다.")
    void create_table_group_with_no_order_table() {
        TableGroup givenTableGroup = new TableGroup(1L, LocalDateTime.now(), new ArrayList<>());

        assertThatThrownBy(() -> tableGroupService.create(givenTableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹 지정시 주문 테이블 1개만 주어지면 예외를 던진다.")
    void create_table_group_with_one_order_table() {
        OrderTable orderTable1 = new OrderTable(1L, null, 3, true);
        List<OrderTable> orderTables = Collections.singletonList(orderTable1);
        TableGroup givenTableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);

        assertThatThrownBy(() -> tableGroupService.create(givenTableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹 저장시 그룹 지정할 주문 테이블 갯수가 다르게 주어지면 예외를 던진다")
    void create_table_group_with_different_order_table() {
        OrderTable orderTable1 = new OrderTable(1L, null, 3, true);
        OrderTable orderTable2 = new OrderTable(2L, null, 2, true);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        TableGroup givenTableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);
        when(orderTableDao.findAllByIdIn(anyList()))
                .thenReturn(Collections.singletonList(orderTable1));

        assertThatThrownBy(() -> tableGroupService.create(givenTableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹 저장시 주문 테이블이 비어있지 않으면 예외를 던진다.")
    void create_table_group_with_not_empty_order_table() {
        OrderTable orderTable1 = new OrderTable(1L, null, 3, false);
        OrderTable orderTable2 = new OrderTable(2L, null, 2, false);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        TableGroup givenTableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);
        when(orderTableDao.findAllByIdIn(anyList()))
                .thenReturn(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(givenTableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("지정할 주문 테이블이 테이블 그룹에 이미 포함되어있으면 예외를 던진다.")
    void create_table_group_with_order_table_has_group_id() {
        OrderTable orderTable1 = new OrderTable(1L, 1L, 3, true);
        OrderTable orderTable2 = new OrderTable(2L, 2L, 2, true);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        TableGroup givenTableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);
        when(orderTableDao.findAllByIdIn(anyList()))
                .thenReturn(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(givenTableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹을 해제한다")
    void ungroup() {
        OrderTable orderTable1 = new OrderTable(1L, 1L, 3, true);
        OrderTable orderTable2 = new OrderTable(2L, 1L, 2, true);
        Long tableGroupId = 1L;
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        when(orderTableDao.findAllByTableGroupId(anyLong()))
                .thenReturn(orderTables);

        tableGroupService.ungroup(tableGroupId);

        verify(orderTableDao, times(2))
                .save(any(OrderTable.class));
    }

    @Test
    @DisplayName("테이블 그룹을 해제할 주문테이블이 완료 상태가 아니면 예외를 던진다.")
    void un_group_table_with_table_status_is_not_complete() {
        Long givenTableGroupId = 1L;
        OrderTable orderTable1 = new OrderTable(1L, 1L, 3, true);
        OrderTable orderTable2 = new OrderTable(2L, 1L, 2, true);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        when(orderTableDao.findAllByTableGroupId(anyLong()))
                .thenReturn(orderTables);
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                anyList(), eq(Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
        ).thenReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(givenTableGroupId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}




