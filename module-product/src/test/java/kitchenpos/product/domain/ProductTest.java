package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import kitchenpos.product.exception.EmptyNameException;
import kitchenpos.product.exception.NegativePriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class ProductTest {

    @DisplayName("Product를 생성할 수 있다.(Name, Price)")
    @Test
    void create01() {
        // given & when & then
        assertThatNoException().isThrownBy(() -> Product.of("상품", BigDecimal.valueOf(10_000)));
    }

    @DisplayName("Product를 생성 시 Name은 필수이다.")
    @ParameterizedTest
    @NullAndEmptySource
    void create02(String name) {
        // when & then
        assertThrows(EmptyNameException.class, () -> Product.of(name, BigDecimal.valueOf(10_000)));
    }

    @DisplayName("Product를 생성 시 Price는 필수이다.")
    @Test
    void create03() {
        // when & then
        assertThrows(NegativePriceException.class, () -> Product.of("메뉴", null));
    }
}