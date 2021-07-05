package kitchenpos.table.service;

import kitchenpos.application.TableGroupService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    List<OrderTable> orderTables;

    @BeforeEach
    public void setup() {
        orderTables = new ArrayList<>();
    }

    @Test
    @DisplayName("단체를 지정 한다")
    public void createOrderTableGroup() {
       // given
        orderTables.add(new OrderTable(5L, 0, true));
        orderTables.add(new OrderTable(6L, 0, true));
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);

        // when
        TableGroup createTableGroup = tableGroupService.create(tableGroup);

        // then
        assertThat(createTableGroup.getId()).isNotNull();
        assertThat(createTableGroup.getOrderTables()).isNotEmpty();
    }

    @Test
    @DisplayName("단체 지정 실패 - 주문 테이블이 하나 일 경우")
    public void createOrderTableGroupFailByOneOrderTable() {
        // given
        orderTables.add(new OrderTable(5L, 0, true));
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);

        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(tableGroup));
    }

    @Test
    @DisplayName("단체 지정 실패 - 주문 테이블이 empty가 아닐 경우")
    public void createOrderTableGroupFailByOnderTableIsNotEmpty() {
        // given
        orderTables.add(new OrderTable(3L, 0, false));
        orderTables.add(new OrderTable(6L, 0, false));
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);

        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(tableGroup));
    }

    @Test
    @DisplayName("단체 지정 실패 - 이미 단체 지정이 된 테이블")
    public void createOrderTableGroupFailByAlreadyExistsTableGroup() {
        // given
        orderTables.add(new OrderTable(9L, 0, false));
        orderTables.add(new OrderTable(10L, 0, false));
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);
        tableGroupService.create(tableGroup);

        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(tableGroup));
    }

    @Test
    @DisplayName("단체 지정 실패 - 기존 데이터베이스에 존재하지 테이블을 포함하여 단체 지정 할 경우")
    public void createOrderTableGroupFailByNotExistsTable() {
        // given
        orderTables.add(new OrderTable(10L, 0, false));
        orderTables.add(new OrderTable(11L, 0, false));
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);

        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(tableGroup));
    }

    @Test
    @DisplayName("단체 지정을 해제 한다")
    public void deleteOrderTableGroup() {
        // given
        OrderTable orderTable = new OrderTable(7L, 0, true);
        orderTables.add(orderTable);
        orderTables.add(new OrderTable(8L, 0, true));
        TableGroup createTableGroup = tableGroupService.create(new TableGroup(LocalDateTime.now(), orderTables));

        // when
        tableGroupService.ungroup(createTableGroup.getId());

        // then
        assertThat(orderTable.getTableGroupId()).isNull();
    }

}
