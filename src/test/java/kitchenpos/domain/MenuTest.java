package kitchenpos.domain;

import kitchenpos.exception.MenuProductException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class MenuTest {

    Menu 메뉴;

    Product 상품1;
    Product 상품2;

    MenuGroup 메뉴그룹;

    MenuProduct 메뉴상품1;
    MenuProduct 메뉴상품2;

    @Test
    @DisplayName("메뉴 가격과 포함된 상품들의 합을 계산해서, 메뉴 가격이 더 크지 않아야한다.")
    void priceCheck() {
        //given
        상품1 = new Product("치킨", new BigDecimal(15000));
        상품2 = new Product("피자", new BigDecimal(25000));
        메뉴그룹 = new MenuGroup(1L, "메뉴그룹");
        메뉴상품1 = new MenuProduct(상품1, 2L);
        메뉴상품2 = new MenuProduct(상품2, 1L);
        메뉴 = new Menu("세트메뉴", new BigDecimal(30000), 메뉴그룹, new MenuProducts(Arrays.asList(메뉴상품1, 메뉴상품2)));

        //when
        메뉴.priceCheck();
    }

    @Test
    @DisplayName("메뉴 가격과 포함된 상품들의 합을 계산해서, 메뉴 가격이 더 크면 오류.")
    void priceCheckException() {
        //given
        상품1 = new Product("치킨", new BigDecimal(15000));
        상품2 = new Product("피자", new BigDecimal(25000));
        메뉴그룹 = new MenuGroup(1L, "메뉴그룹");
        메뉴상품1 = new MenuProduct(상품1, 2L);
        메뉴상품2 = new MenuProduct(상품2, 1L);
        메뉴 = new Menu("세트메뉴", new BigDecimal(100000), 메뉴그룹, new MenuProducts(Arrays.asList(메뉴상품1, 메뉴상품2)));

        //when
        assertThatThrownBy(() -> 메뉴.priceCheck())
                .isInstanceOf(MenuProductException.class)
                .hasMessageContaining(MenuProductException.MENU_PRICE_MORE_EXPENSIVE_PRODUCTS_MSG);
    }
}