package kitchenpos.domain;

import kitchenpos.common.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Price Common VO 테스트")
public class PriceTest {
    @DisplayName("Price 생성 테스트")
    @Test
    void create() {
        BigDecimal priveValue = BigDecimal.valueOf(12_000);
        Price 가격 = new Price(priveValue);

        assertThat(가격.value()).isEqualTo(priveValue);
    }

    @DisplayName("가격은 음수이거나 null 일 수 없다.")
    @Test
    void validateException() {
        assertAll(
                () -> assertThatThrownBy(() -> new Price(null)).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> new Price(BigDecimal.valueOf(-12_000))).isInstanceOf(IllegalArgumentException.class)
        );
    }
}
