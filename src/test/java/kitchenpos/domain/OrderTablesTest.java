package kitchenpos.domain;

import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTablesTest {
    @DisplayName("주문 테이블 목록은 비어있을 수 없다.")
    @Test
    void orderTablesEmptyException() {
        assertThatThrownBy(() -> new OrderTables(new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 테이블 목록에 주문 테이블이 없습니다.");
    }

    @DisplayName("주문 테이블은 2개 이상이야 한다.")
    @Test
    void orderTablesLessThanMinimumException() {
        // given
        OrderTable orderTable = new OrderTable(new NumberOfGuests(4), false);

        // when & then
        assertThatThrownBy(() -> new OrderTables(Arrays.asList(orderTable)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 테이블은 2개 이상 존재해야 합니다.");
    }
}
