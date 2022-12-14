package kitchenpos.product.domain;


import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class ProductTest {
    @DisplayName("상품명은 NULL이거나 공백일 수 없다.")
    @ParameterizedTest
    @NullAndEmptySource
    void createWithNullOrEmptyName(String name) {
        assertThatThrownBy(() -> new Product(name, BigDecimal.valueOf(10000)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품명은 비어있거나 공백일 수 없습니다.");
    }
}
