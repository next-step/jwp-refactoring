package kitchenpos.common.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("가격")
class PriceTest {

    @Test
    public void initTest() {
        Price ten = new Price(BigDecimal.TEN);

        assertThat(ten).isNotNull();
        assertThat(ten).isEqualTo(new Price(BigDecimal.TEN));
        assertThat(ten).isEqualTo(Price.TEN);
    }

    @DisplayName("가격은 0원 이상")
    @Test
    public void exceptionTest() {
        assertThatThrownBy(() -> new Price(BigDecimal.valueOf(-1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 0원 이상이어야 합니다.");
    }

    @DisplayName("가격 덧셈을 테스트한다.")
    @Test
    public void plusTest() {
        Price actualPrice = Price.TEN.plus(new Price(BigDecimal.ONE));

        assertThat(actualPrice.getPrice()).isEqualTo(BigDecimal.valueOf(11));
    }

    @DisplayName("가격의 배수를테스트한다.")
    @Test
    public void timesTest() {
        Price actualPrice = Price.TEN.times(2);

        assertThat(actualPrice.getPrice()).isEqualTo(BigDecimal.valueOf(20));
    }

}