package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("OrderTables 클래스 테스트")
class OrderTablesTest {

    private final TableGroupEntity tableGroup = new TableGroupEntity();

    private final List<OrderTableEntity> orderTables = Arrays.asList(new OrderTableEntity(0, false),
                                                                     new OrderTableEntity(0, true),
                                                                     new OrderTableEntity(5, false));

    @DisplayName("3개의 OrderTable로 OrderTables를 생성한다.")
    @Test
    void create() {
        OrderTables orderTables = new OrderTables();

        orderTables.addAll(this.tableGroup, this.orderTables);

        assertThat(orderTables.get()).hasSize(3);
    }
}
