package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import kitchenpos.common.exception.InvalidArgumentException;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("상품 도메인 테스트")
class ProductTest {

    private static final Integer PRODUCT_PRICE = 10000;
    private static final String PRODUCT_NAME = "상품";

    @Test
    @DisplayName("상품의 가격은 필수이며 0 이상 이어야 한다")
    void createValidatePrice() {
        assertThatThrownBy(() -> Product.of(PRODUCT_NAME, -1))
            .isInstanceOf(InvalidArgumentException.class)
            .hasMessageContaining("이상이어야 합니다.");

        assertThatThrownBy(() -> Product.of(PRODUCT_NAME, null))
            .isInstanceOf(InvalidArgumentException.class)
            .hasMessage("가격은 필수입니다.");
    }

    @Test
    @DisplayName("상품은 이름은 필수이다")
    void createValidateName() {
        assertThatThrownBy(() -> Product.of(null, PRODUCT_PRICE))
            .isInstanceOf(InvalidArgumentException.class)
            .hasMessage("이름은 필수입니다.");
    }

    @Test
    @DisplayName("상품 생성")
    void createProduct() {
        Product product = Product.of(PRODUCT_NAME, PRODUCT_PRICE);
        assertAll(
            () -> assertTrue(product.equalName(PRODUCT_NAME)),
            () -> assertTrue(product.equalPrice(BigDecimal.valueOf(PRODUCT_PRICE)))
        );
    }
}