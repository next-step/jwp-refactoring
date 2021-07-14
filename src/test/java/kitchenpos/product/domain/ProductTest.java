package kitchenpos.product.domain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("상품 domain 테스트")
class ProductTest {

    @Test
    void 상품_도메인_객체_생성() {
        String name = "짜장면";
        BigDecimal price = new BigDecimal(7000);
        Product product = new Product(name, price);
        assertThat(product.getName()).isEqualTo(name);
        assertThat(product.getPrice().price()).isEqualTo(price);
    }

    @Test
    void 상품과_수량_곱셈() {
        BigDecimal expected = BigDecimal.valueOf(14000);
        Product product = new Product("짜장면", new BigDecimal(7000));
        assertThat(product.multiplyQuantity(2)).isEqualTo(expected);
    }
}
