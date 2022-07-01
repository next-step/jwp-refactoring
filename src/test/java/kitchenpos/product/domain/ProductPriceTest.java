package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.product.exception.InvalidProductPriceException;
import org.junit.jupiter.api.Test;
class ProductPriceTest {

    @Test
    void 상품가격_객체생성(){
        BigDecimal price = new BigDecimal(1000);
        ProductPrice productPrice = new ProductPrice(price);
        assertThat(productPrice).isEqualTo(new ProductPrice(price));
    }

    @Test
    void 상품가격_유효성검사(){
        BigDecimal invalidPrice = new BigDecimal(-1000);
        assertThatThrownBy(() -> new ProductPrice(invalidPrice)).isInstanceOf(InvalidProductPriceException.class);
    }
}
