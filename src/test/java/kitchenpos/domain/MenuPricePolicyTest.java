package kitchenpos.domain;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MenuPricePolicyTest {
    @DisplayName("메뉴의 가격은 상품의 총 합보다 클 수 없다")
    @Test
    void testPolicy() {
        // given
        Product 볶음짜장면 = new Product(1L, "볶음짜장면", 8000);
        Product 삼선짬뽕 = new Product(2L, "삼선짬뽕", 8000);

        List<MenuProduct> menuProducts = new ArrayList<>();
        Menu menu = new Menu(1L, "집밥이최고", 16000, new MenuGroup(), menuProducts);
        menuProducts.add(new MenuProduct(1L, menu, 볶음짜장면, 1));
        menuProducts.add(new MenuProduct(2L, menu, 삼선짬뽕, 1));

        // when
        MenuPricePolicy.validate(menu);

        // then
        assertTrue(true);
    }

    @DisplayName("메뉴의 가격이 상품 가격의 합보다 크면 안된다")
    @Test
    void testPolicyException() {
        // given
        Product 볶음짜장면 = new Product(1L, "볶음짜장면", 8000);
        Product 삼선짬뽕 = new Product(2L, "삼선짬뽕", 8000);

        List<MenuProduct> menuProducts = new ArrayList<>();
        Menu menu = new Menu(1L, "집밥이최고", 17000, new MenuGroup(), menuProducts);
        menuProducts.add(new MenuProduct(1L, menu, 볶음짜장면, 1));
        menuProducts.add(new MenuProduct(2L, menu, 삼선짬뽕, 1));

        // when
        ThrowableAssert.ThrowingCallable callable = () -> MenuPricePolicy.validate(menu);

        // then
        assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
    }
}
