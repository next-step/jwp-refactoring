package kitchenpos.product.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class ProductTest {

    @DisplayName("상품 객체를 생성한다.")
    @Test
    void constructor() {
        // when
        Product product = new Product("name", BigDecimal.valueOf(100));

        // then
        assertThat(product).isNotNull();
        assertThat(product.getName()).isEqualTo("name");
    }

    @DisplayName("상품 객체 생성 예외 - 상품 금액은 0보다 커야 한다.")
    @Test
    void validatePrice() {
        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> new Product("name", BigDecimal.valueOf(-1)))
            .withMessage("상품 금액은 0보다 커야 한다.");
    }
}
