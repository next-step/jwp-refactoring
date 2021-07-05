package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class PriceTest {
    @Test
    @DisplayName("가격이 비어 있거나, 0원보다 적을경우 IllegalArgumentException이 발생한다")
    void 가격이_비어_있거나_0원보다_적을경우_IllegalArgumentException이_발생한다() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Price(null));
        assertThatIllegalArgumentException().isThrownBy(() -> new Price(new BigDecimal(-1)));
    }

    @Test
    @DisplayName("가격이 0보다 크면 정상이다")
    void 가격이_0보다_크면_정상이다() {
        assertDoesNotThrow(() -> new Price(new BigDecimal(100)));
    }

    @Test
    void multiply() {
        // given
        Price price1 = new Price(2);
        Price price2 = new Price(5);

        // when
        Price result = price1.multiply(price2);

        // then
        assertThat(result).isEqualTo(new Price(10));
    }
}