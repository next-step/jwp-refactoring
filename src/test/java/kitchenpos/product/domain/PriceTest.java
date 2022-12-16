package kitchenpos.product.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class PriceTest {
    @DisplayName("상품 가격을 null로 생성할 수 없다.")
    @Test
    void 상품_가격을_null로_생성() {
        assertThatIllegalArgumentException().isThrownBy(() -> Price.of(null));
    }

    @DisplayName("상품 가격을 음수로 생성할 수 없다.")
    @ParameterizedTest
    @ValueSource(ints = {-1000, -50000})
    void 상품_가격을_음수로_생성(long price) {
        assertThatIllegalArgumentException().isThrownBy(() -> Price.of(BigDecimal.valueOf(price)));
    }
}
