package kitchenpos.order.domain;

import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
        OrderTable orderTable = OrderTableFixture.생성(7,true);
        OrderTables orderTables = new OrderTables();

        orderTables.add(orderTable);

        assertThat(orderTables.getOrderTables().contains(orderTable)).isTrue();
    }

    @DisplayName("단체지정 해제")
    @Test
    void ungroup() {
        OrderTable orderTable1 = OrderTableFixture.생성(4, true);
        OrderTable orderTable2 = OrderTableFixture.생성(7,true);
        OrderTables orderTables = new OrderTables();
        orderTables.add(orderTable1);
        orderTables.add(orderTable2);

        orderTables.ungroup();

        assertThat(orderTables.getOrderTables().size()).isEqualTo(0);
    }
}
