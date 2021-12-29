package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.price.domain.Price;
import kitchenpos.exception.KitchenposException;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;

class MenuTest {

    @DisplayName("메뉴의 합보다 금액이 많을 시 에러")
    @Test
    void constructError() {
        MenuGroup menuGroup = new MenuGroup("name");
        Product product = new Product("product", new Price(BigDecimal.valueOf(5)));
        MenuProducts menuProducts = new MenuProducts(Arrays.asList(new MenuProduct(product, 2)));

        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> new Menu(1L, "name", new Price(BigDecimal.valueOf(11)), menuGroup, menuProducts))
        ;
    }
}