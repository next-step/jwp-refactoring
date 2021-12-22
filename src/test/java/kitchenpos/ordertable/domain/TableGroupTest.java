package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @DisplayName("테이블 그룹 생성")
    @Test
    void constructor() {
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, 6, true),
            new OrderTable(2L, 3, true));

        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);
        assertThat(tableGroup.getOrderTables()).containsAll(orderTables);
    }
}
