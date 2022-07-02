package kitchenpos.menu.domain;

import kitchenpos.exception.IllegalPriceException;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static kitchenpos.utils.fixture.MenuFixtureFactory.*;
import static kitchenpos.utils.fixture.MenuGroupFixtureFactory.*;
import static kitchenpos.utils.fixture.MenuProductFixtureFactory.*;
import static kitchenpos.utils.fixture.ProductFixtureFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("메뉴 도메인 테스트")
class MenuTest {
    @DisplayName("메뉴를 생성한다")
    @Test
    void Menu_생성() {
        MenuGroup menuGroup_한식 = createMenuGroup("한식");
        Product product_김치찌개 = createProduct("김치찌개", 8000);
        MenuProduct menuProduct_김치찌개 = createMenuProduct(product_김치찌개, 1);
        Menu menu = createMenu("김치찌개", 8000, menuGroup_한식, Arrays.asList(menuProduct_김치찌개));

        assertAll(
                () -> assertThat(menu.getMenuProducts()).containsExactly(menuProduct_김치찌개),
                () -> assertThat(menuProduct_김치찌개.getMenu()).isEqualTo(menu)
        );
    }

    @DisplayName("메뉴의 가격은 0 이상이다")
    @Test
    void Menu_가격_0이상_검증(){
        MenuGroup menuGroup_한식 = createMenuGroup("한식");
        Product product_김치찌개 = createProduct("김치찌개", 8000);
        MenuProduct menuProduct_김치찌개 = createMenuProduct(product_김치찌개, 1);

        assertThrows(IllegalPriceException.class,
                () -> Menu.of("김치찌개", -8000, menuGroup_한식, Arrays.asList(menuProduct_김치찌개)));
    }

    @DisplayName("메뉴에 메뉴상품을 등록한다")
    @Test
    void Menu_MenuProduct_등록(){
        MenuGroup menuGroup_한식 = createMenuGroup("한식");
        Product product_김치찌개 = createProduct("김치찌개", 8000);
        MenuProduct menuProduct_김치찌개 = createMenuProduct(product_김치찌개, 1);
        Menu menu = createMenu("김치찌개", 8000, menuGroup_한식, Arrays.asList(menuProduct_김치찌개));

        assertThat(menu.getMenuProducts()).containsExactly(menuProduct_김치찌개);
    }

    @DisplayName("메뉴의 가격은, 메뉴상품의 정가의 합보다 클 수 없다")
    @Test
    void Menu_가격_정가이하_검증(){
        MenuGroup menuGroup_한식 = createMenuGroup("한식");
        Product product_김치찌개 = createProduct("김치찌개", 8000);
        MenuProduct menuProduct_김치찌개 = createMenuProduct(product_김치찌개, 1);

        assertThrows(IllegalPriceException.class,
                () -> Menu.of("김치찌개", 10000, menuGroup_한식, Arrays.asList(menuProduct_김치찌개)));
    }
}