package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.product.exception.ProductPriceNegativeException;
import kitchenpos.product.exception.ProductPriceEmptyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("상품 테스트")
public class ProductTest {

    @DisplayName("상품의 가격은 필수 입력항목이다.")
    @Test
    void price_null() {
        assertThatThrownBy(() -> new Product("상품명", null))
            .isInstanceOf(ProductPriceEmptyException.class);
    }

    @DisplayName("상품의 가격은 음수가 될수 없다.")
    @Test
    void price_negative() {
        assertThatThrownBy(() -> new Product("상품명", new BigDecimal(-1)))
            .isInstanceOf(ProductPriceNegativeException.class);
    }

}
