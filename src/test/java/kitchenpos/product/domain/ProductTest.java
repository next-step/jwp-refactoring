package kitchenpos.product.domain;

import kitchenpos.product.exception.IllegalProductPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @DisplayName("상품 등록(생성)에 성공한다.")
    @Test
    void createProduct() {
        String name = "상품";
        BigDecimal price = BigDecimal.valueOf(20000L);
        Product product = new Product(name, price);

        assertThat(product).isEqualTo(new Product(name, price));
    }

    @DisplayName("상품 등록(생성)에 실패한다. - 상품 등록시 가격값이 null 이면 등록 실패한다.")
    @Test
    void fail_create1() {
        String name = "상품";
        BigDecimal price = null;

        assertThatThrownBy(() -> new Product(name, price))
                .isInstanceOf(IllegalProductPriceException.class);
    }

    @DisplayName("상품 등록(생성)에 실패한다. - 상품 등록시 가격값이 0보다 작으면 등록 실패한다.")
    @Test
    void fail_create2() {
        String name = "상품";
        BigDecimal price = BigDecimal.valueOf(-1L);

        assertThatThrownBy(() -> new Product(name, price))
                .isInstanceOf(IllegalProductPriceException.class);
    }
}
