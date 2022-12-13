package kitchenpos.product.domain;

import kitchenpos.common.Name;
import kitchenpos.common.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatNoException;

@DisplayName("상품")
class ProductTest {

    @DisplayName("상품 생성")
    @Test
    void create() {
        assertThatNoException().isThrownBy(() -> new Product(new Name("name"), new Price(BigDecimal.ONE)));
    }
}
