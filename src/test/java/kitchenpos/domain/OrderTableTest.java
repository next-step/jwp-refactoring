package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {
    public static final OrderTable EMPTY_TABLE = new OrderTable(1, true);

    @Test
    void 빈_테이블인지_확인한다() {
        // when & then
        assertThatThrownBy(() ->
                EMPTY_TABLE.validateEmpty()
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블은 주문을 할 수 없습니다.");
    }
}
