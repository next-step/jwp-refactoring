package kitchenpos.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.Price;

public class PriceTest {

    @Test
    @DisplayName("Price 생성테스트")
    void create() {
        Price price = Price.of(BigDecimal.valueOf(100));
        assertThat(price.toBigDecimal()).isEqualTo(BigDecimal.valueOf(100));
    }

    @Test
    @DisplayName("음수는 불가능하다.")
    void validation() {
        assertThrows(IllegalArgumentException.class, () -> {
            Price.of(BigDecimal.valueOf(-1));
        });
    }

}
