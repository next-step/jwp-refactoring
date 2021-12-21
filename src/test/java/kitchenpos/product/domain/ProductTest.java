package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ProductTest {

    @DisplayName("상품 등록")
    @ParameterizedTest
    @CsvSource(value = {"초코퍼지,5000", "무료아이스크림,0", "할인케이크,10000.24"})
    void construct(String name, BigDecimal decimal) {
        Product product = new Product(name, decimal);
        Product expectedProduct = new Product(name, decimal);
        assertThat(product.getName()).isEqualTo(expectedProduct.getName());
        assertThat(product.getPrice()).isEqualTo(expectedProduct.getPrice());
    }

    @DisplayName("상품 가격은 0원 이상이어야 한다.")
    @Test
    void construct_exception1() {
        assertThatThrownBy(() -> new Product("잘못된상품", BigDecimal.valueOf(-10000)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 가격은 필수값이다.")
    @Test
    void construct_exception2() {
        assertThatThrownBy(() -> new Product("가격미정 상품", null))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
