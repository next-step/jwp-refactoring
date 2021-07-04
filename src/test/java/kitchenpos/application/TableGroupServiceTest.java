package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@ExtendWith(MockitoExtension.class)
@DisplayName("단체 지정 테스트")
class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    private TableGroup tableGroup;
    private OrderTable orderTable;
    private OrderTable addOrderTable;

    @BeforeEach
    void setup() {
        orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setEmpty(true);
        addOrderTable = new OrderTable();
        addOrderTable.setId(2L);
        addOrderTable.setEmpty(true);

        tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(orderTable, addOrderTable));
    }

    @DisplayName("사용자는 단체 지정을 할 수 있다.")
    @Test
    void create() {
        // given

        // when
        when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(orderTable, addOrderTable));
        when(orderTableDao.save(any())).thenReturn(new OrderTable());
        when(tableGroupDao.save(tableGroup)).thenReturn(tableGroup);

        TableGroup createdTableGroup = tableGroupService.create(tableGroup);
        // then
        assertThat(createdTableGroup).isNotNull();
    }

    @DisplayName("사용자는 단체를 취소 할 수 있다.")
    @Test
    void ungroup() {
        // given
        List<OrderTable> orderTables = Arrays.asList(orderTable, addOrderTable);
        // when
        when(orderTableDao.findAllByTableGroupId(1L)).thenReturn(orderTables);
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(false);
        // then
        tableGroupService.ungroup(1L);
        assertTrue(true);
    }

    @DisplayName("주문테이블의 요청 id의 개수가 2보다 작은지 체크한다.")
    @Test
    void createFailedByOrderTables() {
        // given
        tableGroup.setOrderTables(new ArrayList<>());
        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블의 데이터를 체크한다. 이 때 요청 받은 주문테이블의 데이터가 모두 있는지 체크한다.")
    @Test
    void createFailedByOrderTablesCount() {
        // given

        // when
        when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(orderTable));
        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("조회 시 주문 상태가 요리중, 식사중 상태가아닌지 체크한다.")
    @Test
    void ungroupFailedByCookingStatus() {
        // given
        List<OrderTable> orderTables = Arrays.asList(orderTable, addOrderTable);

        // when
        when(orderTableDao.findAllByTableGroupId(1L)).thenReturn(orderTables);
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(true);

        // then
        assertThatThrownBy(() -> tableGroupService.ungroup(1L)).isInstanceOf(IllegalArgumentException.class);
    }
}