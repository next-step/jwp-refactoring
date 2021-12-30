package kitchenpos.menu.domain;

import kitchenpos.menu.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @DisplayName("상품의 가격이 음수면 예외 발생한다.")
    @Test
    void isNegativeProduct() {
        assertThatThrownBy( () -> Product.of("상품", BigDecimal.valueOf(-100)))
                .isInstanceOf(IllegalArgumentException.class);
    }


}
