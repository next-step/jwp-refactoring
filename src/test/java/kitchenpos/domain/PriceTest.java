package kitchenpos.domain;

import static java.math.BigDecimal.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.exception.InvalidPriceException;

class PriceTest {

    @DisplayName("[가격] 가격은 널값이거나 0이하일 수 없다")
    @Test
    void test1() {
        assertThatThrownBy(() -> new Price(null))
            .isInstanceOf(InvalidPriceException.class);
        assertThatThrownBy(() -> new Price(BigDecimal.valueOf(-10L)))
            .isInstanceOf(InvalidPriceException.class);
        assertDoesNotThrow(() -> new Price(BigDecimal.valueOf(10L)));
        assertDoesNotThrow(() -> new Price(ZERO));
    }
}
