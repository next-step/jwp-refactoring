package kitchenpos.menu.domain;

import kitchenpos.menu.domain.fixture.MenuFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static kitchenpos.menu.domain.fixture.MenuProductFixture.menuProductA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@DisplayName("메뉴 상품 일급 콜렉션")
class MenuProductsTest {

    @DisplayName("메뉴 상품 일급 콜렉션을 생성한다.")
    @Test
    void create() {
        Assertions.assertThatNoException().isThrownBy(MenuProducts::new);
    }

    @DisplayName("메뉴 상품 일급 콜렉션의 empty 여부를 반환한다. / false")
    @Test
    void isEmpty_false() {
        MenuProducts menuProducts = new MenuProducts(Collections.singletonList(MenuProductFixture.menuProductA(1L)));
        assertThat(menuProducts.isEmpty()).isFalse();
    }

    @DisplayName("메뉴 상품 일급 콜렉션의 empty 여부를 반환한다. / true")
    @Test
    void isEmpty_true() {
        MenuProducts menuProducts = new MenuProducts();
        assertThat(menuProducts.isEmpty()).isTrue();
    }

    @DisplayName("메뉴와 매핑한다.")
    @Test
    void mapMenu() {
        MenuProducts menuProducts = new MenuProducts(Collections.singletonList(MenuProductFixture.menuProductA(1L)));
        menuProducts.mapMenu(MenuFixture.menuA(1L));
        for (MenuProduct menuProduct : menuProducts.getMenuProducts()) {
            assertThat(menuProduct.getMenu()).isNotNull();
        }
    }
}
