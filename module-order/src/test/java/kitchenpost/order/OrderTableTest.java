package kitchenpost.order;

import kitchenpos.table.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTableTest {

    @DisplayName("단체 지정 가능한지 확인")
    @Test
    public void checkGroupable() {
        OrderTable orderTable = new OrderTable(0, false);
        assertThatThrownBy(() -> {
            new TableGroup(new ArrayList<OrderTable>() {{
                add(orderTable);
            }});
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정")
    @Test
    public void group() {
        OrderTable orderTable = new OrderTable(0, false);
        orderTable.changeEmpty(true);
        orderTable.group(new TableGroup(new ArrayList<OrderTable>() {{
            add(orderTable);
            add(orderTable);
        }}));
        assertThat(orderTable.isGrouped()).isTrue();
    }

    @DisplayName("단체 지정 해지")
    @Test
    public void ungroup() {
        OrderTable orderTable = new OrderTable(0, false);
        orderTable.changeEmpty(true);
        orderTable.group(new TableGroup(new ArrayList<OrderTable>() {{
            add(orderTable);
            add(orderTable);
        }}));
        orderTable.ungroup();
        assertThat(orderTable.isGrouped()).isFalse();
    }

}
