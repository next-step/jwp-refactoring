package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테이블들 테스트")
class OrderTablesTest {

    @DisplayName("최소 테이블 수 보다 작으면 예외가 발생한다.")
    @Test
    void createLessThanMinimumTables() {
        List<OrderTable> orderTables = new ArrayList<>();

        for (int i = 0; i < OrderTables.MINIMUM_TABLES - 1; i++) {
            orderTables.add(new OrderTable());
        }

        assertThatThrownBy(() -> OrderTables.of(orderTables))
            .isInstanceOf(IllegalArgumentException.class);
    }
}