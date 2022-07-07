package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTableTest {
    private OrderTable 주문_테이블;

    @BeforeEach
    void init() {
        // given
        주문_테이블 = 주문_테이블_생성(1L, 4, true);
    }

    @Test
    @DisplayName("주문 테이블의 방문 손님 수를 0명 이하로 변경할 경우 - 오류")
    void changeNegativeQuantityGuest() {
        // when then
        assertThatThrownBy(() -> 주문_테이블.changeGuests(-5))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("비어 있지 않은 주문 테이블의 손님 수를 변경할 경우 - 오류")
    void changeGuestOfOrderTableIfEmptyIsTrue() {
        // when then
        assertThatThrownBy(() -> 주문_테이블.changeGuests(10))
            .isInstanceOf(IllegalArgumentException.class);
    }

    public static OrderTable 주문_테이블_생성(Long orderTableId, int numberOfGuests, boolean empty) {
        return new OrderTable(orderTableId, numberOfGuests, empty);
    }

    public static OrderTable 주문_테이블_생성(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return new OrderTable(tableGroup, numberOfGuests, empty);
    }

    public static OrderTable 주문_테이블_생성(Long orderTableId, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return new OrderTable(orderTableId, tableGroup, numberOfGuests, empty);
    }
}
