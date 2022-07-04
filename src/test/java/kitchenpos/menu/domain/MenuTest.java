package kitchenpos.menu.domain;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.application.fixture.MenuGroupDtoFixtureFactory;
import kitchenpos.menu.domain.fixture.MenuProductFixtureFactory;
import kitchenpos.product.application.fixture.ProductDtoFixtureFactory;
import kitchenpos.product.domain.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {
    private MenuGroup menuGroup;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        menuGroup = MenuGroupDtoFixtureFactory.createMenuGroup("메뉴그룹1");
        product1 = ProductDtoFixtureFactory.createProduct(1L, "상품1", 1000);
        product2 = ProductDtoFixtureFactory.createProduct(2L, "상품2", 2000);
    }

    @Test
    @DisplayName("메뉴 등록")
    void 메뉴그룹에_메뉴추가() {
        String menuName = "메뉴1";
        int menuPrice = 5000;
        Menu menu = 테스트_메뉴_생성(menuGroup, menuName, menuPrice);
        Assertions.assertAll("저장된 메뉴를 확인한다"
                , () -> assertThat(menu.getMenuGroup()).isSameAs(menuGroup)
                , () -> assertThat(menu.getName()).isEqualTo(menuName)
                , () -> assertThat(menu.getPrice().intValue()).isEqualTo(menuPrice)
        );

        List<MenuProduct> menuProducts = menu.getMenuProducts().toList();
        List<Long> menuProductIds = menuProducts.stream()
                .map(MenuProduct::getProductId)
                .collect(toList());
        Assertions.assertAll("저장된 메뉴의 메뉴 상품을 확인한다"
                , () -> assertThat(menuProducts).hasSize(2)
                , () -> assertThat(menuProductIds).containsExactlyInAnyOrder(product1.getId(), product2.getId())
        );
    }

    private Menu 테스트_메뉴_생성(MenuGroup menuGroup, String menuName, int menuPrice) {
        MenuProduct menuProduct1 = MenuProductFixtureFactory.createMenuProduct(1L, product1.getId(), 4);
        MenuProduct menuProduct2 = MenuProductFixtureFactory.createMenuProduct(2L, product2.getId(), 1);
        return new Menu(menuName, new BigDecimal(menuPrice), menuGroup, Lists.newArrayList(menuProduct1, menuProduct2));
    }
}
