package kitchenpos.domain;

import kitchenpos.common.Money;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DisplayName("메뉴")
class MenuTest {

    private MenuGroup menuGroup;
    private List<MenuProduct> menuProducts;

    @BeforeEach
    void setUp() {
        menuGroup = new MenuGroup("추천메뉴");

        Product 강정치킨 = new Product("강정치킨", Money.valueOf(19000));
        Product 후라이드 = new Product("후라이드", Money.valueOf(15000));
        menuProducts = Arrays.asList(
                new MenuProduct(강정치킨, 2),
                new MenuProduct(후라이드, 2));
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void create() {
        // given
        Money priceOfProducts = menuProducts.stream()
                .map(MenuProduct::price)
                .reduce(Money.zero(), Money::add);

        // when
        Menu menu = new Menu("치킨두마리", priceOfProducts, menuGroup, menuProducts);

        // then
        assertThat(menu).isNotNull();
    }

    @DisplayName("메뉴 상품이 하나 이상 존재해야 한다.")
    @Test
    void requiredMenuProduct() {
        // given
        Money priceOfProducts = menuProducts.stream()
                .map(MenuProduct::price)
                .reduce(Money.zero(), Money::add);

        // when / then
        assertThrows(IllegalArgumentException.class, () ->
                new Menu("치킨두마리", priceOfProducts, menuGroup, new ArrayList<>()));
    }

    @DisplayName("메뉴 가격이 메뉴 상품들의 가격 총합보다 비쌀 수 없다.")
    @Test
    void priceStrategy() {
        // given
        Money overMoney = menuProducts.stream()
                .map(MenuProduct::price)
                .reduce(Money.zero(), Money::add)
                .add(Money.valueOf(100));

        // when / then
        assertThrows(IllegalArgumentException.class, () ->
                new Menu("치킨두마리", overMoney, menuGroup, menuProducts));
    }
}
