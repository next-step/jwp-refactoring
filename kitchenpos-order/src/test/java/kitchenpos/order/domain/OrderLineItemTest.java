package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class OrderLineItemTest {
    @DisplayName("주문 항목의 메뉴는 NULL일 수 없다.")
    @Test
    void createWithMenuIsNull() {
        assertThatThrownBy(() -> new OrderLineItem(null, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴는 필수 값 입니다.");
    }

    @DisplayName("주문 항목의 수량은 0보다 커야 한다.")
    @ParameterizedTest
    @ValueSource(longs = {0, -1})
    void createWithQuantityIsLessThanOne(long quantity) {
        assertThatThrownBy(() -> new OrderLineItem(1L, quantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("수량은 1 이상 이어야 합니다.");
    }
}
