package kitchenpos.core.domain;

import kitchenpos.core.exception.InvalidPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Price 클래스 테스트")
class PriceTest {

    private final Price ten = new Price(BigDecimal.TEN);

    @DisplayName("1인 Price를 생성한다.")
    @Test
    void successfulCreate() {
        Price price = new Price(BigDecimal.ONE);
        assertThat(price.getValue()).isEqualTo(BigDecimal.ONE);
    }

    @DisplayName("-1인 Price를 생성한다.")
    @Test
    void failureCreate() {
        assertThatThrownBy(() -> {
            new Price(BigDecimal.valueOf(-1));
        }).isInstanceOf(InvalidPriceException.class)
        .hasMessageContaining("유효하지 않은 가격입니다.");
    }

    @DisplayName("Price(10)과 Price(10)은 동등하다.")
    @Test
    void equals() {
        assertThat(ten).isEqualTo(new Price(BigDecimal.valueOf(10L)));
    }

    @DisplayName("Price(10)과 Quantity(10)을 곱하면 Amount(100)이다.")
    @Test
    void multiply() {
        assertThat(ten.multiply(new Quantity(10L))).isEqualTo(new Amount(BigDecimal.valueOf(100L)));
    }

    @DisplayName("Price(10)을 Amount(10)으로 변환한다.")
    @Test
    void toAmount() {
        assertThat(ten.toAmount()).isEqualTo(new Amount(BigDecimal.TEN));
    }
}
