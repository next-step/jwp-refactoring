package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductsTest {

    @DisplayName("메뉴 상품들 가격의 합 계산")
    @Test
    void calculateSum() {
        MenuProducts menuProducts = new MenuProducts(
            Arrays.asList(new MenuProduct(new Product("name", BigDecimal.ONE), 2),
                new MenuProduct(new Product("name", BigDecimal.TEN), 10)));

        assertThat(menuProducts.calculateSum()).isEqualTo(new Price(BigDecimal.valueOf(102)));
    }
}