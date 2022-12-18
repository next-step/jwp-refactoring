package kitchenpos.product.domain;

import kitchenpos.product.domain.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;

@DisplayName("상품")
class ProductTest {

    @DisplayName("상품 생성")
    @Test
    void create() {
        assertThatNoException().isThrownBy(ProductFixture::productA);
    }
}
