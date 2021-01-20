package kitchenpos.table;

import kitchenpos.application.TableGroupService;
import kitchenpos.application.TableService;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class OrderTableTest {
    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @MockBean
    private OrderDao orderDao;

    @DisplayName("주문 테이블 등록")
    @Test
    void createOrderTable() {
        OrderTable orderTable = tableService.create(setUpOrderTable(true));

        assertThat(orderTable.getId()).isNotNull();
    }

    @DisplayName("주문 테이블 조회")
    @Test
    void findOrderTable() {
        List<OrderTable> orderTables = tableService.list();

        assertThat(orderTables).isNotEmpty();
    }

    @DisplayName("주문 테이블 상태 변경")
    @Test
    void changeOrderTable() {
        OrderTable orderTable = tableService.create(setUpOrderTable(true));
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(10);

        OrderTable changedEmpty = tableService.changeEmpty(orderTable.getId(), orderTable);
        OrderTable changedGuests = tableService.changeNumberOfGuests(orderTable.getId(), orderTable);

        assertThat(changedEmpty.isEmpty()).isFalse();
        assertThat(changedGuests.getNumberOfGuests()).isEqualTo(10);
    }

    @DisplayName("주문 테이블 상태 변경 실패")
    @Test
    void changeOrderTableFail() {
        OrderTable orderTable = tableService.create(setUpOrderTable(true));
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(-1);
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(true);

        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable));
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable));
    }

    @DisplayName("테이블 그룹화")
    @Test
    void applyGroup() {
        OrderTable orderTable1 = tableService.create(setUpOrderTable(true));
        OrderTable orderTable2 = tableService.create(setUpOrderTable(true));
        TableGroup tableGroup = setUpTableGroup(orderTable1, orderTable2);

        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        assertThat(savedTableGroup.getId()).isNotNull();
        assertThat(savedTableGroup.getOrderTables()).isNotEmpty();
    }

    @DisplayName("테이블 그룹화 실패")
    @Test
    void applyGroupFail() {
        OrderTable orderTable1 = tableService.create(setUpOrderTable(true));
        OrderTable orderTable2 = tableService.create(setUpOrderTable(false));
        TableGroup tableGroup = setUpTableGroup(orderTable1, orderTable2);

        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("테이블 그룹화 해제")
    @Test
    void applyUnGroup() {
        OrderTable orderTable1 = tableService.create(setUpOrderTable(true));
        OrderTable orderTable2 = tableService.create(setUpOrderTable(true));
        TableGroup tableGroup = setUpTableGroup(orderTable1, orderTable2);

        TableGroup savedTableGroup = tableGroupService.create(tableGroup);
        tableGroupService.ungroup(savedTableGroup.getId());

        OrderTable findOrderTable = tableService.list().stream()
                .filter(orderTable -> Objects.equals(orderTable.getId(), orderTable1.getId()))
                .findFirst().get();

        assertThat(findOrderTable.getTableGroupId()).isNull();
    }

    @DisplayName("테이블 그룹화 해제 실패")
    @Test
    void applyUnGroupFail() {
        OrderTable orderTable1 = tableService.create(setUpOrderTable(true));
        OrderTable orderTable2 = tableService.create(setUpOrderTable(true));
        TableGroup tableGroup = setUpTableGroup(orderTable1, orderTable2);
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).thenReturn(true);

        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()));
    }

    private OrderTable setUpOrderTable(boolean flag) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(flag);
        return orderTable;
    }

    private TableGroup setUpTableGroup(OrderTable orderTable1, OrderTable orderTable2) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));
        return tableGroup;
    }
}
