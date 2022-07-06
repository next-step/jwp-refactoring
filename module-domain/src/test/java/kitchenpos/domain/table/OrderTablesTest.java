package kitchenpos.domain.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTablesTest {
    @Test
    @DisplayName("최소 2개이상이어야 한다.")
    void validateSize() {
        OrderTable orderTable = OrderTable.of(4, false);

        assertThatThrownBy(() -> {
            OrderTables.of(Lists.list(orderTable));
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("OrderTables 를 생성한다.")
    void create() {
        OrderTable orderTable = OrderTable.of(4, false);
        OrderTable otherTable = OrderTable.of(4, false);

        OrderTables orderTables = OrderTables.of(Lists.list(orderTable, otherTable));

        assertThat(orderTables).isNotNull();
    }
}
