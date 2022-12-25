package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.common.error.ErrorEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class PriceTest {

    @ParameterizedTest
    @ValueSource(ints = {1,1_000})
    void 메뉴_가격을_생성한다(int value) {
        Price price = new Price(BigDecimal.valueOf(value));

        assertThat(price.value()).isEqualTo(BigDecimal.valueOf(value));
    }

    @Test
    void 메뉴_가격은_NULL_일_수_없다() {
        assertThatThrownBy(() -> new Price(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.PRICE_IS_NOT_NULL.message());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -100})
    void 메뉴_가격은_0원_이상이어야_한다(int value) {
        assertThatThrownBy(() -> new Price(BigDecimal.valueOf(value)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.PRICE_UNDER_ZERO.message());
    }
}
