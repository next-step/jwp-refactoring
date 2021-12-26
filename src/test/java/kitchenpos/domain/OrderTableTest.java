package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {

    @DisplayName("그룹테이블에 속해있는 테이블을 빈테이블로 변경할 수 없다.")
    @Test
    void changeEmpty() {
        //given
        TableGroup tableGroup = TableGroup.of(1L, null, null);
        OrderTable orderTable = OrderTable.of(1L, tableGroup, 4, false);
        //then
        assertThatThrownBy(
                () -> orderTable.changeEmpty(true)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태가 조리중이거나 식사중인 테이블을 빈테이블로 변경한다.")
    @Test
    void changeEmpty2() {
        //given
        OrderTable orderTable = OrderTable.of(1L, null, 4, false);
        OrderLineItem orderLineItem = OrderLineItem.of(1L, null, null, 1);
        Order.of(orderTable, OrderStatus.COOKING, Arrays.asList(orderLineItem));

        //then
        assertThatThrownBy(
                () -> orderTable.changeEmpty(true)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블의 손님 수는 변경할 수 없다.")
    @Test
    void changeNumberOfGuests() {
        //given
        OrderTable orderTable = OrderTable.of(1L, null, 4, true);

        // then
        assertThatThrownBy(
                () -> orderTable.changeNumberOfGuests(2)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
