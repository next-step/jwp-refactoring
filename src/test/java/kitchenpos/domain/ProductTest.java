package kitchenpos.domain;

import kitchenpos.domain.exceptions.product.InvalidProductPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {
    @DisplayName("상품의 가격이 없거나 0원 미만으로 오브젝트를 생성할 수 없다.")
    @Test
    void createFailTest() {
        String productName = "product";
        BigDecimal productPrice = BigDecimal.valueOf(-1);

        assertThatThrownBy(() -> new Product(productName, productPrice))
                .isInstanceOf(InvalidProductPriceException.class)
                .hasMessage("상품의 가격은 반드시 있어야 하며, 0원 이상이어야 합니다.");
    }
}