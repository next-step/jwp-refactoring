package kitchenpos.menu.domain;

import kitchenpos.menu.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceTest {
    @DisplayName("특정 값을 가진 가격을 생성할 수 있다.")
    @Test
    void createPrice() {
        // given
        BigDecimal 값 = BigDecimal.valueOf(10000);

        // when
        Price 가격 = Price.from(값);

        // then
        assertThat(가격.getValue())
                .isEqualTo(값);
    }

    @DisplayName("가격은 0보다 작은 값이 될 수 없다.")
    @Test
    void validateValue() {
        // given
        BigDecimal 값 = BigDecimal.valueOf(-10000);

        // when & then
        assertThatThrownBy(() -> Price.from(값))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 0보다 작을 수 없습니다.");
    }
}
