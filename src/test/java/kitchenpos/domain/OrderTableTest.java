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

    @Test
    @DisplayName("단체 지정할 수 있는지 확인할 때 비어 있는 상태라면 예외가 발생한다")
    void groupIsEmptyException() {
        OrderTable orderTable = new OrderTable(new TableGroup(new OrderTables()), 5, false);

        assertThatThrownBy(
                () -> orderTable.changeEmpty(true))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    @DisplayName("주문 테이블 목록에 대해 단체 지정 해제를 할 수 있다")
    void orderTablesUngroup() {
        OrderTable orderTableA = new OrderTable(3, true);
        OrderTable orderTableB = new OrderTable(5, true);
        OrderTables orderTables = new OrderTables(Arrays.asList(orderTableA, orderTableB));
        TableGroup tableGroup = new TableGroup(orderTables);

        orderTableA.addTableGroup(tableGroup);
        orderTableB.addTableGroup(tableGroup);

        orderTables.ungroup();

        assertThat(orderTableA.getTableGroup()).isNull();
        assertThat(orderTableB.getTableGroup()).isNull();
    }
}
