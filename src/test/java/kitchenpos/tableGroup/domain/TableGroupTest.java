package kitchenpos.tableGroup.domain;

import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.order.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TableGroupTest {

    @DisplayName("단체 지정 생성")
    @Test
    void addOrderTables() {
        OrderTable orderTable = OrderTableFixture.생성(4, true);
        TableGroup tableGroup = new TableGroup(Arrays.asList(orderTable));

        assertThat(tableGroup.getOrderTables().contains(orderTable)).isTrue();
    }

    @DisplayName("단체 지정 해제")
    @Test
    void ungroup() {
        OrderTable orderTable = OrderTableFixture.생성(4, true);
        TableGroup tableGroup = new TableGroup(Arrays.asList(orderTable));
        tableGroup.ungroup();

        assertAll(
                () -> assertThat(tableGroup.getOrderTables().contains(orderTable)).isFalse(),
                () -> assertThat(orderTable.getTableGroup()).isNull()
        );
    }
}
