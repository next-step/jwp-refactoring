package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.product.domain.fixture.ProductFixtureFactory;
import kitchenpos.product.exception.InvalidProductPriceException;
import org.junit.jupiter.api.Test;

class ProductPriceTest {

    @Test
    void 상품가격_객체생성() {
        BigDecimal price = new BigDecimal(1000);
        ProductPrice productPrice = ProductFixtureFactory.createProductPrice(price);
        assertThat(productPrice).isEqualTo(ProductFixtureFactory.createProductPrice(price));
    }

    @Test
    void 상품가격_유효성검사() {
        BigDecimal invalidPrice = new BigDecimal(-1000);
        assertThatThrownBy(() -> ProductFixtureFactory.createProductPrice(invalidPrice))
                .isInstanceOf(InvalidProductPriceException.class);
    }
}
