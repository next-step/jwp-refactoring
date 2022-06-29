package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    @DisplayName("이름값이 존재 하야합니다.")
    void productNameValidationTest() {
        assertAll(
                () -> assertThatIllegalArgumentException().isThrownBy(() -> new Product("", BigDecimal.valueOf(1000))),
                () -> assertThatIllegalArgumentException().isThrownBy(() -> new Product(null, BigDecimal.valueOf(1000)))
        );
    }

    @Test
    @DisplayName("가격은 0원 이상이여야 합니다.")
    void productPriceValidationTest() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Product("짬뽕", BigDecimal.valueOf(-1)));
    }
}
