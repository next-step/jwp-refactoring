package kitchenpos.menu.domain;

import kitchenpos.menu.exception.MenuPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MenuTest {

    @DisplayName("메뉴가격이 없으면 예외발생")
    @Test
    public void throwsExceptionWhenNullAmount() {
        assertThatThrownBy(() -> Menu.builder().price(null).build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴가격이 0보다작으면 예외발생")
    @Test
    public void throwsExceptionWhenNetativeAmount() {
        assertThatThrownBy(() -> Menu.builder().price(BigDecimal.valueOf(-1000)).build())
                .isInstanceOf(MenuPriceException.class)
                .hasMessageContaining("가격은 0보다 작을수 없습니다");

    }

    @DisplayName("메뉴에서 메뉴상품을 조회할경우 메뉴에 포함되있는 메뉴상품 반환")
    @Test
    public void returnMenuProducts() {
        List<MenuProduct> menuProductList = Arrays.asList(
                MenuProduct.builder()
                        .productId(1l)
                        .quantity(1)
                        .build(),
                MenuProduct.builder()
                        .productId(2l)
                        .quantity(2)
                        .build()
        );
        Menu menu = Menu.builder().price(BigDecimal.valueOf(1000)).build();
        MenuProducts products = MenuProducts.of(menuProductList);
        menu.addMenuProducts(products);

        assertThat(menu.getMenuProducts()).containsAll(menuProductList);
    }
}
