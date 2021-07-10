package kitchenpos.table.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TableGroupTest {

    private List<OrderTable> orderTableList;

    @BeforeEach
    public void setup() {
        orderTableList = new ArrayList<>();
    }

    @Test
    @DisplayName("단체를 지정 한다")
    public void createOrderTableGroup() {
       // given
        orderTableList.add(new OrderTable(0, TableStatus.EMPTY));
        orderTableList.add(new OrderTable(0, TableStatus.EMPTY));
        OrderTables orderTables = new OrderTables(orderTableList);
        LocalDateTime createDate = LocalDateTime.now();
        Long id = 100L;

        // when
        TableGroup tableGroup = new TableGroup(id, createDate, orderTables);

        // then
        assertThat(tableGroup).isEqualTo(new TableGroup(id, createDate, orderTables));
    }

    @Test
    @DisplayName("단체 지정을 해제 한다")
    public void deleteOrderTableGroup() {
        // given
        OrderTable orderTable = new OrderTable(0, TableStatus.EMPTY);
        orderTableList.add(orderTable);
        orderTableList.add(new OrderTable(0, TableStatus.EMPTY));
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), new OrderTables(orderTableList));

        // when
        tableGroup.unGroup();

        // then
        assertThat(orderTable.tableGroupId()).isNull();
    }

}
