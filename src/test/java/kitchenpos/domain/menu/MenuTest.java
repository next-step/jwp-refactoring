package kitchenpos.domain.menu;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.fixture.MenuGroupFixtureFactory;
import kitchenpos.application.fixture.ProductFixtureFactory;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.product.Product;
import kitchenpos.exception.CreateMenuException;
import kitchenpos.exception.EmptyNameException;
import kitchenpos.exception.MenuPriceException;
import kitchenpos.exception.NegativePriceException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

class MenuTest {

    private MenuGroup 메뉴그룹;
    private Product 상품_1;
    private Product 상품_2;

    @BeforeEach
    void setUp() {
        메뉴그룹 = MenuGroupFixtureFactory.create("메뉴그룹");
        상품_1 = ProductFixtureFactory.create("상품_1", BigDecimal.valueOf(10_000));
        상품_2 = ProductFixtureFactory.create("상품_2", BigDecimal.valueOf(20_000));
    }

    @DisplayName("Menu를 생성할 수 있다.(Name, Price, MenuGroup, MenuProducts")
    @Test
    void create01() {
        // given
        List<MenuProduct> menuProducts = Lists.newArrayList(
                MenuProduct.of(상품_1, 1L),
                MenuProduct.of(상품_2, 1L)
        );

        // when
        Menu menu = Menu.of("메뉴", BigDecimal.valueOf(30_000), 메뉴그룹);
        menu.appendAllMenuProducts(menuProducts);

        // then
        assertAll(
                () -> assertEquals("메뉴", menu.findName()),
                () -> assertEquals(BigDecimal.valueOf(30_000), menu.findPrice()),
                () -> assertEquals(메뉴그룹, menu.getMenuGroup()),
                () -> assertEquals(menuProducts, menu.findMenuProducts())
        );
    }

    @DisplayName("Menu 생성 시 MenuGroup이 없으면 생성할 수 없다.")
    @ParameterizedTest
    @NullSource
    void create02(MenuGroup menuGroup) {
        // when & then
        assertThrows(CreateMenuException.class, () -> Menu.of("메뉴", BigDecimal.valueOf(30_000), menuGroup));
    }

    @DisplayName("Menu 생성 시 Name이 없으면 생성할 수 없다.")
    @ParameterizedTest
    @NullSource
    void create03(String name) {
        // when & then
        assertThrows(EmptyNameException.class, () -> Menu.of(name, BigDecimal.valueOf(30_000), 메뉴그룹));
    }

    @DisplayName("Menu 생성 시 Price가 없으면 생성할 수 없다.")
    @ParameterizedTest
    @NullSource
    void create04(BigDecimal price) {
        // when & then
        assertThrows(NegativePriceException.class, () -> Menu.of("메뉴", price, 메뉴그룹));
    }

    @DisplayName("Menu 생성 시 Menu의 Price는 구성하는 Product의 Price 총 합보다 클 수 없다.")
    @Test
    void create05() {
        // given
        BigDecimal menuPrice = BigDecimal.valueOf(10_000_000);
        List<MenuProduct> menuProducts = Lists.newArrayList(
                MenuProduct.of(상품_1, 1L),
                MenuProduct.of(상품_2, 1L)
        );

        Menu 메뉴 = Menu.of("메뉴", menuPrice, 메뉴그룹);

        // when & then
        assertThrows(MenuPriceException.class, () -> 메뉴.appendAllMenuProducts(menuProducts));
    }
}