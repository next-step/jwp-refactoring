package kitchenpos.domain;

import static kitchenpos.exception.ErrorCode.PRICE_IS_NULL_OR_MINUS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.exception.KitchenposException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

public class PriceTest {
    @Test
    void 생성() {
        Price price = Price.of(BigDecimal.valueOf(10000));
        assertThat(price).isEqualTo(Price.of(BigDecimal.valueOf(10000)));
    }

    @ParameterizedTest
    @NullSource
    void NULL로_생성한_경우(BigDecimal price) {
        assertThatThrownBy(
                () -> Price.of(price)
        )
                .isInstanceOf(KitchenposException.class)
                .hasMessageContaining(PRICE_IS_NULL_OR_MINUS.getDetail());
    }

    @Test
    void 음수로_생성한_경우() {
        assertThatThrownBy(
                () -> Price.of(BigDecimal.valueOf(-5000))
        )
                .isInstanceOf(KitchenposException.class)
                .hasMessageContaining(PRICE_IS_NULL_OR_MINUS.getDetail());
    }
}
