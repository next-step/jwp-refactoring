package kitchenpos.global;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("Price Domain Test")
public class PriceTest {
    @Test
    void validNullTest() {
        assertThatThrownBy(() -> {
            new Price(null);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void validNegativeTest() {
        assertThatThrownBy(() -> {
            new Price(new BigDecimal(-100));
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void addTest() {
        // given
        Price price = new Price(new BigDecimal(1000));

        // when
        price = price.add(new Price(new BigDecimal(2000)));

        // then
        assertThat(price.getValue()).isEqualTo(new BigDecimal(3000));
    }
}
