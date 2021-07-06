package kitchenpos.table.domain;

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
public class TableGroupRepositoryTest {

    @Autowired
    private TableGroupRepository tableGroupRepository;

    private List<OrderTable> orderTableList;

    @BeforeEach
    public void setup() {
        orderTableList = new ArrayList<>();
    }

    @Test
    @DisplayName("단체를 지정 한다")
    public void createOrderTableGroup() {
       // given
        orderTableList.add(new OrderTable(5L, 0, true));
        orderTableList.add(new OrderTable(6L, 0, true));
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), new OrderTables(orderTableList));

        // when
        TableGroup saveTableGroup = tableGroupRepository.save(tableGroup);
        // then
        assertThat(saveTableGroup).isEqualTo(tableGroup);
    }

    @Test
    @DisplayName("단체 지정을 해제 한다")
    public void deleteOrderTableGroup() {
        // given
        OrderTable orderTable = new OrderTable(7L, 0, true);
        orderTableList.add(orderTable);
        orderTableList.add(new OrderTable(8L, 0, true));
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), new OrderTables(orderTableList));

        // when
        tableGroup.unGroup();

        // then
        assertThat(orderTable.tableGroup()).isNull();
    }

}
