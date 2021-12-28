package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MenuTest {

    @Test
    @DisplayName("메뉴 상품들의 총 가격을 계산한다.")
    void sumMenuProductPrice() {
        // given
        MenuGroup menuGroup = MenuGroup.of("두마리메뉴");

        int priceValue = 16_000;
        Product product = Product.of("후라이드", new BigDecimal(priceValue));

        int quantity1 = 1;
        int quantity2 = 2;
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(MenuProduct.of(product, quantity1));
        menuProducts.add(MenuProduct.of(product, quantity2));
        Menu menu = Menu.of("후라이드치킨", new BigDecimal(priceValue), menuGroup, menuProducts);

        // when
        BigDecimal result = menu.sumMenuProductPrice();

        // then
        assertThat(result).isEqualTo(new BigDecimal((priceValue * quantity1) + (priceValue * quantity2)));
    }
}