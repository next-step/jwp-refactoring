package kitchenpos.table.domain;

import kitchenpos.table.exception.IllegalOrderTablesSizeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTablesTest {

    @DisplayName("요청할 주문 테이블 갯수가 2개 이상이면 orderTables가 생성된다.")
    @Test
    void createOrderTables() {
        OrderTable orderTable1 = new OrderTable(null, 4, true);
        OrderTable orderTable2 = new OrderTable(null, 3, true);
        List<OrderTable> tables = Arrays.asList(orderTable1, orderTable2);

        OrderTables ordertables = new OrderTables(tables);

        assertThat(ordertables).isEqualTo(new OrderTables(tables));
    }

    @DisplayName("OrderTables 생성에 실패한다 - 요청할 주문 테이블 갯수가 없거나 2개 미만일 경우 실패")
    @Test
    void fail_createOrderTables() {
        OrderTable orderTable = new OrderTable(null, 3, true);
        List<OrderTable> tables = Arrays.asList(orderTable);

        assertThatThrownBy(() -> new OrderTables(tables))
                .isInstanceOf(IllegalOrderTablesSizeException.class);
    }
}
