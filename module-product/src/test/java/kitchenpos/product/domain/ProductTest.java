package kitchenpos.product.domain;

import kitchenpos.product.domain.fixture.ProductFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

@DisplayName("상품")
class ProductTest {

    @DisplayName("상품 생성")
    @Test
    void create() {
        Assertions.assertThatNoException().isThrownBy(ProductFixture::product);
    }

    @DisplayName("이름을 필수로 갖는다.")
    @Test
    void name() {
        Assertions.assertThatThrownBy(() -> new Product(1L, null, new Price(BigDecimal.ONE)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격을 필수로 갖는다.")
    @Test
    void price() {
        Assertions.assertThatThrownBy(() -> new Product(1L, new Name("A"), null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
