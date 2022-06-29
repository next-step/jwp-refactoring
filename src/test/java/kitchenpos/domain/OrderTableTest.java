package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    OrderTable orderTable1;
    OrderTable orderTable2;
    TableGroup tableGroup;

    @BeforeEach
    void before() {
        orderTable1 = new OrderTable(0, true);
        orderTable2 = new OrderTable(0, true);
        tableGroup = new TableGroup(Arrays.asList(orderTable1, orderTable2));
    }
    @Test
    @DisplayName("주문 테이블에 단체 지정을 할 수 잇다.")
    void attachTest() {
        //given
        OrderTable orderTable = new OrderTable(0, true);

        //when
        orderTable.attachToTableGroup(tableGroup);

        //then
        assertThat(orderTable.getTableGroup()).isNotNull();
    }

    @Test
    @DisplayName("주문 테이블에 손님수를 변경 할 수 잇다.")
    void guestTest() {
        //given
        OrderTable orderTable = new OrderTable(0, true);

        //when
        orderTable.updateNumberOfGuests(5);

        //then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(5);
    }


    @Test
    @DisplayName("주문 테이블을 빈테이블로 변경 할 수 잇다.")
    void emptyTest() {
        //given
        OrderTable nonEmptyTable = new OrderTable(3, false);
        Order order = new Order(nonEmptyTable.getId(), OrderStatus.COMPLETION, new OrderLineItem(1L, 10));

        //when
        nonEmptyTable.clear(order);

        //then
        assertThat(nonEmptyTable.isEmpty()).isTrue();
    }
}
