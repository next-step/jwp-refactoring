package kitchenpos.manu.domain;

import kitchenposNew.menu.domain.Product;
import kitchenposNew.wrap.Price;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProductTest {

    @Test
    void 상품_생성() {
        Product product = new Product("치킨", new Price(BigDecimal.valueOf(17000)));
        assertThat(product).isEqualTo(new Product("치킨", new Price(BigDecimal.valueOf(17000))));
    }

    @Test
    void 상품_가격_예외() {
        assertThatThrownBy(() -> new Price(BigDecimal.valueOf(-17000)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
