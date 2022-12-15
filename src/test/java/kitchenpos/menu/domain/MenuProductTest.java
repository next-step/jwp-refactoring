package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MenuProductTest {
    @Test
    @DisplayName("메뉴 상품 하나의 가격을 계산한다 메뉴 상품 * 상품 갯수")
    void calMenuProductPrice() {
        Product product = Product.of(1L,"test",BigDecimal.valueOf(1000));
        MenuProduct menuProduct = MenuProduct.of(null, product, 1);

        assertThat(menuProduct.getPrice()).isEqualTo(BigDecimal.valueOf(1000));
    }
}