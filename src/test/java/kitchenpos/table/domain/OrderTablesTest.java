package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTablesTest {

    @DisplayName("주문 테이블 목록 생성한다.")
    @Test
    void createOrderTables() {
        // given
        OrderTable 주문_테이블1 = new OrderTable(1L, null, 3, true);
        OrderTable 주문_테이블2 = new OrderTable(2L, null, 5, true);
        List<OrderTable> 주문테이블_목록 = Arrays.asList(주문_테이블1, 주문_테이블2);

        // when, then
        new OrderTables(주문테이블_목록);
    }

    @DisplayName("주문 테이블 목록 개수를 검증한다.")
    @Test
    void validateSizeForTableGroup_size() {
        // given
        OrderTable 주문_테이블1 = new OrderTable(1L, null, 3, true);
        OrderTable 주문_테이블2 = new OrderTable(2L, null, 5, true);
        OrderTables 주문_테이블_목록 = new OrderTables(Arrays.asList(주문_테이블1, 주문_테이블2));

        // when
        assertThatThrownBy(() -> {
            주문_테이블_목록.validateSizeForTableGroup(3);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 사용중이면 안된다.")
    @Test
    void validateSizeForTableGroup_empty() {
        // given
        OrderTable 주문_테이블1 = new OrderTable(1L, null, 3, false);
        OrderTable 주문_테이블2 = new OrderTable(2L, null, 5, true);
        OrderTables 주문_테이블_목록 = new OrderTables(Arrays.asList(주문_테이블1, 주문_테이블2));

        // when
        assertThatThrownBy(() -> {
            주문_테이블_목록.validateSizeForTableGroup(2);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
