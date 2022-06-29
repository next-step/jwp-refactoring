package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTablesTest {
    private OrderTable orderTable;
    private OrderTables orderTables;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable.Builder()
                .setEmpty(true)
                .build();

        orderTables = new OrderTables(Arrays.asList(orderTable, orderTable));
    }

    @Test
    @DisplayName("객체가 같은지 검증")
    void verifyEqualsOrderTables() {
        assertThat(orderTables).isEqualTo(new OrderTables(Arrays.asList(orderTable, orderTable)));
    }
}
