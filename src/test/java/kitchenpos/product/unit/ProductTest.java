package kitchenpos.product.unit;

import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @DisplayName("상품 생성에 성공한다.")
    @Test
    void 생성() {
        // when
        Product 샐러드 = new Product("샐러드", BigDecimal.valueOf(100));

        // then
        assertThat(샐러드).isNotNull();
        assertThat(샐러드.getName()).isEqualTo("샐러드");
        assertThat(샐러드.getUnitPrice()).isEqualTo(BigDecimal.valueOf(100));
    }

    @DisplayName("상품 가격이 0보다 작으면 상품 생성에 실패한다.")
    @Test
    void 생성_예외_가격_오류() {
        // when, then
        assertThatThrownBy(() -> new Product("샐러드", BigDecimal.ZERO.subtract(BigDecimal.ONE)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
