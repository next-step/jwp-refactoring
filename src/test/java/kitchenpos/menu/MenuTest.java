package kitchenpos.menu;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MenuTest {
    @Test
    @DisplayName("메뉴를 구성한다.")
    void createMenu() {
        Product 짜장면 = new Product("짜장면", new BigDecimal(5000));
        Product 탕수육 = new Product("탕수육", new BigDecimal(15000));
        MenuProduct 짜장면상품 = new MenuProduct(짜장면, 1);
        MenuProduct 탕수육상품 = new MenuProduct(탕수육, 1);

        Menu menu = new Menu("짜장면셋트", new BigDecimal(20000));
        menu.organizeMenu(Arrays.asList(짜장면상품, 탕수육상품));

        assertThat(menu.getMenuProducts().getMenuProducts()).hasSize(2);
    }

    @Test
    @DisplayName("메뉴의 가격과 메뉴 상품 가격의 총 합이 다르면 메뉴 구성에 실패한다.")
    void isNotSamePrice() {
        Product 짜장면 = new Product("짜장면", new BigDecimal(5000));
        Product 탕수육 = new Product("탕수육", new BigDecimal(20000));
        MenuProduct 짜장면상품 = new MenuProduct(짜장면, 1);
        MenuProduct 탕수육상품 = new MenuProduct(탕수육, 1);

        Menu menu = new Menu("짜장면셋트", new BigDecimal(20000));

        assertThatThrownBy(() -> menu.organizeMenu(Arrays.asList(짜장면상품, 탕수육상품)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("메뉴 가격이 올바르지 않습니다");
    }
}
