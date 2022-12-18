package kitchenpos.domain;

import kitchenpos.domain.type.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class OrderTablesTest {

    @Test
    @DisplayName("주문 테이블 목록은 비어있을 수 없다.")
    void orderTableIsEmptyException() {
        assertThatThrownBy(
                () -> new OrderTables(new ArrayList<>())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블은 2개 이상이이야 한다")
    void orderTablesLessThanMinimumException() {
        OrderTable orderTable = new OrderTable(7, false);

        assertThatThrownBy(
                () -> new OrderTables(Arrays.asList(orderTable))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정할 수 있는지 확인할 때, 빈 상태가 아니라면 예외가 발생한다.")
    void groupEmptyException() {
        OrderTable orderTable = new OrderTable(5, false);
        Order 주문 = new Order(orderTable, OrderStatus.COOKING, null);

        assertThatThrownBy(
                () -> orderTable.changeEmpty(true, Arrays.asList(주문)))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("주문 테이블 목록에 대해 단체 지정해제를 할 수 있다.")
    @Test
    void orderTablesUngroup() {
        // given
        OrderTable orderTableA = new OrderTable(3, true);
        OrderTable orderTableB = new OrderTable(5, true);
        OrderTables orderTables = new OrderTables(Arrays.asList(orderTableA, orderTableB));
        TableGroup tableGroup = new TableGroup(orderTables);

        orderTableA.addTableGroup(tableGroup);

        // when
        orderTables.ungroup();

        // then
        assertThat(orderTableA.getTableGroup()).isNull();
    }
}
