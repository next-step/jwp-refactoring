package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.exception.KitchenposException;

class OrderTablesTest {

    @DisplayName("사용중인 주문테이블이 있으면 에러")
    @Test
    void checkNotContainsUsedTable() {
        List<OrderTable> orderTables = Collections.singletonList(
            new OrderTable(1L, null, 4, false));

        OrderTables tables = new OrderTables(orderTables);

        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(tables::checkNotContainsUsedTable)
            .withMessage("사용중인 테이블이 있습니다.");
    }

    @Test
    void unGroup() {
        List<OrderTable> orderTables = Collections.singletonList(
            new OrderTable(1L, new TableGroup(1L), 4, true));

        OrderTables tables = new OrderTables(orderTables);
        tables.unGroup();

        assertThat(tables.getOrderTables().get(0).getTableGroup()).isNull();
    }
}