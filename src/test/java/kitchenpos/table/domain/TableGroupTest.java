package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableGroupTest {

    @Test
    @DisplayName("단체 지정을 생선한다.")
    void constructor() {
        // given
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(OrderTable.of(4, true));
        orderTables.add(OrderTable.of(4, true));

        // when
        TableGroup result = new TableGroup(LocalDateTime.now(), orderTables);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getCreatedDate()).isNotNull();
        assertThat(result.getOrderTables().size()).isEqualTo(orderTables.size());
    }

    @Test
    @DisplayName("주문 테이블이 없으면 단체 지정을 생성할 수 없다.")
    void constructor_order_tables_empty() {
        // given
        List<OrderTable> orderTables = new ArrayList<>();

        // when, then
        assertThatThrownBy(() -> new TableGroup(LocalDateTime.now(), orderTables))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("단체 지정할 주문 테이블은 2개 이상이어야 합니다.");
    }

    @Test
    @DisplayName("주문 테이블이 1개이면 단체 지정을 생성할 수 없다.")
    void constructor_order_tables_one() {
        // given
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(OrderTable.of(4, true));

        // when, then
        assertThatThrownBy(() -> new TableGroup(LocalDateTime.now(), orderTables))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("단체 지정할 주문 테이블은 2개 이상이어야 합니다.");
    }
}