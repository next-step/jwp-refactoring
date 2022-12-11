package kitchenpos.domain;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTablesTest {
    @DisplayName("주문 테이블 목록은 비어있을 수 없다.")
    @Test
    void orderTablesEmptyException() {
        assertThatThrownBy(() -> new OrderTables(new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.ORDER_TABLES_IS_EMPTY.getMessage());
    }

    @DisplayName("주문 테이블은 2개 이상이야 한다.")
    @Test
    void orderTablesLessThanMinimumException() {
        // given
        OrderTable orderTable = new OrderTable(new NumberOfGuests(4), false);

        // when & then
        assertThatThrownBy(() -> new OrderTables(Arrays.asList(orderTable)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.ORDER_TABLES_MINIMUM_IS_TWO.getMessage());
    }
}
