package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Price 클래스 테스트")
class PriceTest {

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
        Price ten = new Price(BigDecimal.TEN);
        assertThat(ten).isEqualTo(new Price(BigDecimal.valueOf(10L)));
    }
}