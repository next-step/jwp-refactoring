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

@SpringBootTest
public class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    private TableGroup tableGroup;
    private OrderTable orderTable;

    @BeforeEach
    public void setup() {
        List<OrderTable> orderTables = new ArrayList<>();
        orderTable = new OrderTable(1L, 0, false);
        OrderTable orderTable2 = new OrderTable(2L, 0, false);
        orderTables.add(orderTable);
        orderTables.add(orderTable2);
        tableGroup = new TableGroup(LocalDateTime.now(), orderTables);
    }

    @Test
    @DisplayName("단체를 지정 한다")
    public void createOrderTableGroup() {
        // when
        TableGroup createTableGroup = tableGroupService.create(this.tableGroup);

        // then
        assertThat(createTableGroup.getId()).isNotNull();
        assertThat(createTableGroup.getOrderTables()).isNotEmpty();
    }

    @Test
    @DisplayName("단체 지정을 해제 한다")
    public void deleteOrderTableGroup() {
        // given
        TableGroup createTableGroup = tableGroupService.create(this.tableGroup);

        // when
        tableGroupService.ungroup(createTableGroup.getId());

        // then
        assertThat(orderTable.getTableGroupId()).isNull();
    }

}
