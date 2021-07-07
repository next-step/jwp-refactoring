package kitchenpos.tablegroup.application;

import kitchenpos.order.dao.OrderDao;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.tablegroup.dao.TableGroupDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.application.TableGroupService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

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

    @DisplayName("테이블 그룹 생성 테스트")
    @Test
    void createTest() {
        // given
        OrderTable orderTable1 = new OrderTable(null, 3);
        OrderTable orderTable2 = new OrderTable(null, 4);
        TableGroup tableGroup = new TableGroup(Arrays.asList(orderTable1, orderTable2));
        orderTable1.setEmpty(true);
        orderTable2.setEmpty(true);

        Mockito.when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(orderTable1, orderTable2));
        Mockito.when(tableGroupDao.save(any())).thenReturn(tableGroup);

        // when
        TableGroup actual = tableGroupService.create(tableGroup);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.getOrderTables()).isNotEmpty().hasSize(2);
    }

    @DisplayName("테이블 그룹으로 묶일 주문 테이블이 2개 이하일 경우")
    @Test
    void createTestWrongSize() {
        // given
        OrderTable orderTable = new OrderTable(null, 3);
        TableGroup tableGroup = new TableGroup(Arrays.asList(orderTable));

        // when
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹으로 묶일 주문 테이블이 빈 테이블이 아닌 경우")
    @Test
    void createTestWrongOrderTable() {
        // given
        OrderTable orderTable1 = new OrderTable(null, 3);
        OrderTable orderTable2 = new OrderTable(1L, 4);
        TableGroup tableGroup = new TableGroup(Arrays.asList(orderTable1, orderTable2));

        Mockito.when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(orderTable1, orderTable2));

        // when
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("테이블 그룹 해제 테스트")
    @Test
    void ungroupTest() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, 3);
        OrderTable orderTable2 = new OrderTable(1L, 4);

        Mockito.when(orderTableDao.findAllByTableGroupId(any())).thenReturn(Arrays.asList(orderTable1, orderTable2));
        Mockito.when(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(false);

        // when
        tableGroupService.ungroup(1L);

        // then
        Mockito.verify(orderTableDao, Mockito.times(2)).save(any());
    }

    @DisplayName("테이블 그룹으로 묶일 주문 테이블의 상태가 COMPLETION이 아닐 경우")
    @Test
    void ungroupTestWithWrongStatus() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, 3);
        OrderTable orderTable2 = new OrderTable(1L, 4);

        Mockito.when(orderTableDao.findAllByTableGroupId(any())).thenReturn(Arrays.asList(orderTable1, orderTable2));
        Mockito.when(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(true);

        // when
        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
