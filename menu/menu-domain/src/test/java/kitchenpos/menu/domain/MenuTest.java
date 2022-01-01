package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.exception.KitchenposException;
import kitchenpos.common.price.domain.Price;

class MenuTest {
    @DisplayName("메뉴의 합보다 금액이 많을 시 에러")
    @Test
    void validatePrice() {
        MenuProducts menuProducts = new MenuProducts(Arrays.asList(new MenuProduct(1L, 2)));

        Menu menu = new Menu(1L, "name", new Price(BigDecimal.valueOf(11)), 1L, menuProducts);
        Price sum = new Price(BigDecimal.valueOf(10));

        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> menu.validatePrice(sum));
    }
}