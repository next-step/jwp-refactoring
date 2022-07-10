package kitchenpos.application;

import io.restassured.specification.RequestSpecification;
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

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

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
    public void setUp() {
        tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
    }

    @DisplayName("단체를 지정한다.")
    @Test
    void create() {
        OrderTable firstOrderTable = new OrderTable();
        firstOrderTable.setEmpty(true);

        OrderTable secondOrderTable = new OrderTable();
        secondOrderTable.setEmpty(true);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setOrderTables(Arrays.asList(firstOrderTable, secondOrderTable));

        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(firstOrderTable, secondOrderTable));
        given(tableGroupDao.save(any())).willReturn(tableGroup);

        TableGroup result = tableGroupService.create(tableGroup);
        assertNotNull(result.getId());
    }

    @DisplayName("주문 테이블의 수가 2 미만인 경우 단체를 지정할 수 없다.")
    @Test
    void createWithOneOrderTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(1L);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(orderTable));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체를 해제한다")
    @Test
    void ungroup() {
        OrderTable firstOrderTable = new OrderTable();
        firstOrderTable.setTableGroupId(1L);
        firstOrderTable.setEmpty(false);

        OrderTable secondOrderTable = new OrderTable();
        secondOrderTable.setTableGroupId(1L);
        secondOrderTable.setEmpty(false);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setOrderTables(Arrays.asList(firstOrderTable, secondOrderTable));

        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(Arrays.asList(firstOrderTable, secondOrderTable));

        tableGroupService.ungroup(1L);

        assertAll(
                () -> assertNull(firstOrderTable.getTableGroupId()),
                () -> assertNull(secondOrderTable.getTableGroupId())
        );
    }

    @DisplayName("COOKING 혹은 MEAL 상태인 주문 테이블의 단체는 해제할 수 없다.")
    @Test
    void ungroupOfInvalidStatusTable() {
        OrderTable firstOrderTable = new OrderTable();
        firstOrderTable.setId(1L);
        firstOrderTable.setTableGroupId(1L);
        firstOrderTable.setEmpty(false);

        OrderTable secondOrderTable = new OrderTable();
        secondOrderTable.setId(2L);
        secondOrderTable.setTableGroupId(1L);
        secondOrderTable.setEmpty(false);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setOrderTables(Arrays.asList(firstOrderTable, secondOrderTable));

        given(orderTableDao.findAllByTableGroupId(1L)).willReturn(Arrays.asList(firstOrderTable, secondOrderTable));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                  Arrays.asList(firstOrderTable.getId(), secondOrderTable.getId())
                , Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(1L)).isInstanceOf(IllegalArgumentException.class);
    }
}
