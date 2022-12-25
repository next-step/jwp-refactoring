package kitchenpos.product.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class ProductTest {
    @DisplayName("상품 생성 테스트 - 가격 null")
    @Test
    void validatePriceTest1() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Product("상품", null));
    }

    @DisplayName("상품 생성 테스트 - 올바르지 않는 가격인경우")
    @ParameterizedTest
    @ValueSource(ints = { -1, -1000 })
    void validatePrice2(long price) {
        assertThatIllegalArgumentException().isThrownBy(() -> new Product("감자튀김", BigDecimal.valueOf(price)));
    }

}
