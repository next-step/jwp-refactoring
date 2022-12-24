package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.product.domain.ProductPrice;

@DisplayName("상품 가격 테스트")
class ProductPriceTest {
    @Test
    @DisplayName("동등성")
    void equals() {
        assertThat(ProductPrice.from(10000)).isEqualTo(ProductPrice.from(10000));
        assertThat(ProductPrice.from(10000)).isNotEqualTo(ProductPrice.from(12000));
    }

    @Test
    @DisplayName("가격 없음")
    void product_price_null() {
        // given
        BigDecimal price = null;

        // when, then
        assertThatThrownBy(() -> ProductPrice.from(price))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("가격 0원 미만")
    void product_price_negative() {
        // given
        BigDecimal price = BigDecimal.valueOf(-10000);

        // when, then
        assertThatThrownBy(() -> ProductPrice.from(price))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("대소 비교")
    void isLessThan() {
        // given
        BigDecimal price = BigDecimal.valueOf(10000);

        // when, then
        assertThat(ProductPrice.from(10000).isLessThan(price)).isFalse();
        assertThat(ProductPrice.from(9000).isLessThan(price)).isTrue();
    }
}
