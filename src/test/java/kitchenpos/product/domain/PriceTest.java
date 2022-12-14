package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceTest {
    @DisplayName("가격은 NULL일 수 없다.")
    @Test
    void createWithNull() {
        assertThatThrownBy(() -> new Price(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 필수 값 입니다.");
    }

    @DisplayName("가격은 0원 이하 일 수 없다.")
    @Test
    void createWithNegative() {
        assertThatThrownBy(() -> new Price(BigDecimal.valueOf(-100)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 0원 이하 일 수 없습니다.");
    }
}
