package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.price.domain.Price;

class MenuProductTest {

    @DisplayName("메뉴 상품 전체 가격 계산")
    @Test
    void calculatePrice() {
        // given
        MenuProduct menuProduct = new MenuProduct(1L, 2);
        Price price = new Price(BigDecimal.TEN);

        // when
        Price result = menuProduct.calculatePrice(price);

        // then
        assertThat(result)
            .isEqualTo(new Price(BigDecimal.valueOf(20)));
    }
}