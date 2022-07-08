package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PriceTest {
    @Test
    @DisplayName("Price 생성 시 null일 경우 Exception")
    void createPriceIsNull() {
        assertThatThrownBy(() -> new Price(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Price 생성 시 음수일 경우 Exception")
    void createPriceIsMinus() {
        assertThatThrownBy(() -> new Price(BigDecimal.valueOf(-16_000)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
