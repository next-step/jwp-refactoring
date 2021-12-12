package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class MenuProductTest {

    private static final MenuProduct menuProduct = new MenuProduct(new Product("상품", 10_000), 10);

    @Test
    void calculatePrice_메뉴상품의_가격과_수량의_곱을_계산한다() {
        assertThat(menuProduct.calculatePrice()).isEqualTo(BigDecimal.valueOf(100_000));
    }
}