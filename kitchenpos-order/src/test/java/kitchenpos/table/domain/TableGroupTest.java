package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TableGroupTest {
    private final List<OrderTable> orderTables = Arrays.asList(new OrderTable(0, true),
        new OrderTable(0, true),
        new OrderTable(0, true));

    @DisplayName("단체 지정을 생성한다.")
    @Test
    void create() {
        TableGroup tableGroup = new TableGroup();
        assertThat(tableGroup.getCreatedDate()).isNotNull();
    }

    @DisplayName("단체 지정에 테이블들을 추가한다.")
    @Test
    void addOrderTables() {
        TableGroup tableGroup = new TableGroup();

        tableGroup.addOrderTables(orderTables);

        assertThat(tableGroup.getOrderTables()).hasSize(3);
    }

    @DisplayName("단체 지정을 해지한다.")
    @Test
    void ungroup() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.addOrderTables(orderTables);

        tableGroup.ungroup();

        assertThat(tableGroup.getOrderTables()).isEmpty();
    }
}
