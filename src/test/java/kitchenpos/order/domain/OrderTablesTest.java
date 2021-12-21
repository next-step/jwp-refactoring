package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderTablesTest {

    @DisplayName("주문테이블 일급콜렉션 생성")
    @Test
    void create() {
        OrderTables orderTables = new OrderTables();

        assertThat(orderTables).isNotNull();
    }

    @DisplayName("주문테이블 추가")
    @Test
    void add() {
        OrderTable orderTable = new OrderTable(7,true);
        OrderTables orderTables = new OrderTables();

        orderTables.add(orderTable);

        assertThat(orderTables.getOrderTables().contains(orderTable)).isTrue();
    }
}
