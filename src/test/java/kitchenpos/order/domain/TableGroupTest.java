package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("테이블 그룹 단위 Test")
class TableGroupTest {

    @Test
    @DisplayName("주문을 그룹화한다.")
    void createOrderTableGroup() {
        OrderTable orderTable = new OrderTable(1L, null, 2, false);
        OrderTable orderTable2 = new OrderTable(2L, null, 5, false);

        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(orderTable, orderTable2));

        assertThat(tableGroup.getOrderTables()).contains(orderTable, orderTable2);
    }

    @Test
    @DisplayName("주문을 그룹화하지 않는다.")
    void deleteOrderTableGroup() {
        OrderTable orderTable = new OrderTable(1L, null, 2, false);
        OrderTable orderTable2 = new OrderTable(2L, null, 5, false);

        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(orderTable, orderTable2));
        tableGroup.cancleGroup();
        assertThat(tableGroup.getOrderTables().size()).isEqualTo(0);
    }
}
