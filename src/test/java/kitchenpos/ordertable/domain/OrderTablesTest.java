package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTablesTest {

    private OrderTable 주문테이블;

    @BeforeEach
    void setUp() {
        주문테이블 = OrderTable.of(4, true);
    }

    @DisplayName("주문 테이블 목록에 주문 테이블이 없으면 에러가 발생한다.")
    @Test
    void validateOrderTableIsEmptyException() {
        assertThatThrownBy(() -> OrderTables.from(Collections.emptyList()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 목록에 주문 테이블이 2개 미만이면 에러가 발생한다.")
    @Test
    void validateOrderTableMinSizeException() {
        assertThatThrownBy(() -> OrderTables.from(Arrays.asList(주문테이블)))
            .isInstanceOf(IllegalArgumentException.class);
    }
}