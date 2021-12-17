package kitchenpos.domain.menu.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class MenuProductTest {

    private static final MenuProduct menuProduct = new MenuProduct(1L, 10);

    @Test
    void calculatePrice_메뉴상품의_가격과_수량의_곱을_계산한다() {
        assertThat(menuProduct.calculatePrice(BigDecimal.valueOf(10_000))).isEqualTo(BigDecimal.valueOf(100_000));
    }
}
