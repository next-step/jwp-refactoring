package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTablesTest {
    private OrderTable orderTable1;
    private OrderTable orderTable2;
    private List<OrderTable> orderTableList = new ArrayList<>();
    private OrderTables orderTables = new OrderTables();

    void setUp() {
        orderTable1 = new OrderTable(1L, 4, true);
        orderTables.addTable(orderTable1);
    }

    @Test
    @DisplayName("그룹핑시 최소 테이블 수 체크: 예외처리")
    void tableNumberCheck() {
        assertThatThrownBy(() -> {
            orderTables.checkOrderTables();
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 상태 체크: 예외처리")
    void checkTableStatus() {
        orderTables.addTable(new OrderTable(1L, 4, false));
        assertThatThrownBy(() -> {
            orderTables.checkTableStatus();
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
