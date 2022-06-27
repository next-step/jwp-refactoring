package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("OrderTables 클래스 테스트")
class OrderTablesTest {

    private final Long tableGroupId = 1L;
    private final List<OrderTable> orderTables = Arrays.asList(new OrderTable(0, false),
                                                               new OrderTable(0, true),
                                                               new OrderTable(5, false));

    @DisplayName("3개의 OrderTable로 OrderTables를 생성한다.")
    @Test
    void create() {
        OrderTables orderTables = new OrderTables();

        orderTables.addAll(tableGroupId, this.orderTables);

        assertThat(orderTables.get()).hasSize(3);
    }

    @DisplayName("OrderTables를 초기화한다.")
    @Test
    void clear() {
        OrderTables orderTables = new OrderTables();
        orderTables.addAll(tableGroupId, this.orderTables);

        orderTables.clear();

        assertThat(orderTables.get()).hasSize(0);
    }
}
