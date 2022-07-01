package kitchenpos.menu.domain;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.fixture.MenuGroupFixtureFactory;
import kitchenpos.fixture.MenuProductFixtureFactory;
import kitchenpos.fixture.ProductFixtureFactory;
import kitchenpos.menu.exception.InvalidMenuPriceException;
import kitchenpos.product.domain.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {
    private MenuGroup menuGroup;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        menuGroup = MenuGroupFixtureFactory.createMenuGroup("메뉴그룹1");
        product1 = ProductFixtureFactory.createProduct(1L, "상품1", 1000);
        product2 = ProductFixtureFactory.createProduct(2L, "상품2", 2000);
    }

    @Test
    @DisplayName("메뉴 등록")
    void 메뉴그룹에_메뉴추가() {
        String menuName = "메뉴1";
        int menuPrice = 5000;
        Menu menu = 테스트_메뉴_생성(menuGroup, menuName, menuPrice);

        assertThat(menu.getMenuGroup()).isSameAs(menuGroup);
        assertThat(menu.getName()).isEqualTo(menuName);
        assertThat(menu.getPrice().intValue()).isEqualTo(menuPrice);

        List<MenuProduct> menuProducts = menu.getMenuProducts();
        List<Long> menuProductIds = menuProducts.stream()
                .map(MenuProduct::getProductId)
                .collect(toList());
        assertThat(menuProducts).hasSize(2);
        assertThat(menuProductIds).containsExactlyInAnyOrder(product1.getId(), product2.getId());
    }

    @Test
    @DisplayName("메뉴가격이 음수인 경우 메뉴 등록 실패")
    void 메뉴그룹에_메뉴추가_가격이_음수인경우() {
        String menuName = "메뉴1";
        int menuPrice = -1 * 1000;
        assertThatThrownBy(() -> 테스트_메뉴_생성(menuGroup, menuName, menuPrice))
                .isInstanceOf(InvalidMenuPriceException.class);
    }

    @Test
    @DisplayName("메뉴가격이 상품가격의 합보다 큰 경우 메뉴 등록 실패")
    void 메뉴그룹에_메뉴추가_가격이_상품가격의_합보다_큰경우() {
        String menuName = "메뉴1";
        int menuPrice = 10000;
        Menu menu = 테스트_메뉴_생성(menuGroup, menuName, menuPrice);
        List<Product> products = Lists.newArrayList(product1, product2);
        assertThatThrownBy(() -> menu.checkSumPriceOfProducts(products))
                .isInstanceOf(InvalidMenuPriceException.class);
    }

    private Menu 테스트_메뉴_생성(MenuGroup menuGroup, String menuName, int menuPrice) {
        MenuProduct menuProduct1 = MenuProductFixtureFactory.createMenuProduct(1L, product1.getId(), 4);
        MenuProduct menuProduct2 = MenuProductFixtureFactory.createMenuProduct(2L, product2.getId(), 1);
        return new Menu(menuName, new BigDecimal(menuPrice), menuGroup, Lists.newArrayList(menuProduct1, menuProduct2));
    }
}
