package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class MenuProductTest {

    @Test
    @DisplayName("메뉴 상품의 가격을 구한다.")
    void sumProductPrice() {
        // given
        int priceValue = 16_000;
        int quantity = 2;
        Product product = Product.of("후라이드", new BigDecimal(priceValue));
        MenuProduct menuProduct = MenuProduct.of(product, quantity);

        // when
        BigDecimal result = menuProduct.sumProductPrice();

        // then
        assertThat(result).isEqualTo(new BigDecimal(priceValue * quantity));
    }
}