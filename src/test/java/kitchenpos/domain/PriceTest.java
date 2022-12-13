package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PriceTest {

    @Test
    void multiply() {
        // when
        Price price = Price.from(BigDecimal.ONE).multiply(Quantity.from(5));

        // then
        assertThat(price).isEqualTo(Price.from(BigDecimal.valueOf(5)));
    }

    @Test
    void add() {
        // when
        Price price = Price.from(BigDecimal.ONE);

        assertThat(price.add(Price.from(BigDecimal.TEN))).isEqualTo(Price.from(BigDecimal.valueOf(11)));
    }
}
