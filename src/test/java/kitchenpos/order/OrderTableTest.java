package kitchenpos.order;

import kitchenpos.table.dto.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTableTest {

    @DisplayName("단체 지정 가능한지 확인")
    @Test
    public void checkGroupable() {
        OrderTable orderTable = new OrderTable();
        assertThatThrownBy(orderTable::checkGroupable).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정")
    @Test
    public void group() {
        OrderTable orderTable = new OrderTable();
        orderTable.group(new TableGroup());
        assertThat(orderTable.isGrouped()).isTrue();
    }

    @DisplayName("단체 지정 해지")
    @Test
    public void ungroup() {
        OrderTable orderTable = new OrderTable();
        orderTable.group(new TableGroup());
        orderTable.ungroup();
        assertThat(orderTable.isGrouped()).isFalse();
    }

}
