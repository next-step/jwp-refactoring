package kitchenpos.domain;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {

    @Test
    @DisplayName("주문 테이블 목록은 비어있을 수 없다.")
    void orderTableIsEmptyException() {
        assertThatThrownBy(
                () -> new OrderTables(new ArrayList<>())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블은 2개 이상이이야 한다")
    void orderTablesLessMinSizeTwo() {
        OrderTable orderTable = new OrderTable(7, false);

        assertThatThrownBy(
                () -> new OrderTables(Arrays.asList(orderTable))
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
