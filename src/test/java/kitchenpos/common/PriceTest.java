package kitchenpos.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static kitchenpos.common.Price.PRICE_MINIMUM_EXCEPTION_MESSAGE;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("가격")
class PriceTest {

    @DisplayName("가격을 생성한다.")
    @Test
    void create() {
        assertThatNoException().isThrownBy(() -> new Price(BigDecimal.ONE));
    }

    @DisplayName("메뉴의 가격이 음수일 수 없다.")
    @Test
    void create_fail_minimum() {
        assertThatThrownBy(() -> new Price(BigDecimal.valueOf(-1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(PRICE_MINIMUM_EXCEPTION_MESSAGE);
    }
}
