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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("단체 지정 테스트")
class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    private TableGroupService tableGroupService;
    private TableGroup tableGroup;

    private OrderTable orderTable;
    private OrderTable addOrderTable;

    @BeforeEach
    void setup() {
        tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

        orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setEmpty(true);
        addOrderTable = new OrderTable();
        addOrderTable.setId(2L);
        addOrderTable.setEmpty(true);

        tableGroup = new TableGroup();
        tableGroup.setOrderTables(new ArrayList<>(Arrays.asList(orderTable, addOrderTable)));
    }

    @DisplayName("생성")
    @Test
    void create() {
        // given

        // when
        when(orderTableDao.findAllByIdIn(any())).thenReturn(new ArrayList<>(Arrays.asList(orderTable, addOrderTable)));
        when(orderTableDao.save(any())).thenReturn(new OrderTable());
        when(tableGroupDao.save(tableGroup)).thenReturn(tableGroup);

        TableGroup createdTableGroup = tableGroupService.create(tableGroup);
        // then
        assertThat(createdTableGroup).isNotNull();
    }

    @DisplayName("단체 취소")
    @Test
    void ungroup() {
        // given
        List<OrderTable> orderTables = new ArrayList<>(Arrays.asList(orderTable, addOrderTable));
        // when
        when(orderTableDao.findAllByTableGroupId(1L)).thenReturn(orderTables);
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(false);
        // then
        tableGroupService.ungroup(1L);
        assertTrue(true);
    }

    @DisplayName("생성 실패 - 주문테이블 부족")
    @Test
    void createFailedByOrderTables() {
        // given
        tableGroup.setOrderTables(new ArrayList<>());
        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("생성 실패 - 주문테이블 개수가 다름")
    @Test
    void createFailedByOrderTablesCount() {
        // given

        // when
        when(orderTableDao.findAllByIdIn(any())).thenReturn(new ArrayList<>(Arrays.asList(orderTable)));
        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 취소 실패 - 주문이 요리중임")
    @Test
    void ungroupFailedByCookingStatus() {
        // given
        List<OrderTable> orderTables = new ArrayList<>(Arrays.asList(orderTable, addOrderTable));

        // when
        when(orderTableDao.findAllByTableGroupId(1L)).thenReturn(orderTables);
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(true);

        // then
        assertThatThrownBy(() -> tableGroupService.ungroup(1L)).isInstanceOf(IllegalArgumentException.class);
    }
}