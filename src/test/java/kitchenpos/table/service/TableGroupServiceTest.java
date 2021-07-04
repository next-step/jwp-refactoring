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

        // when
        TableGroup createTableGroup = tableGroupService.create(new TableGroup(LocalDateTime.now(), orderTables));

        // then
        assertThat(createTableGroup.getId()).isNotNull();
        assertThat(createTableGroup.getOrderTables()).isNotEmpty();
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
