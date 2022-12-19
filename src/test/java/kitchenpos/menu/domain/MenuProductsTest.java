package kitchenpos.menu.domain;

import kitchenpos.menu.domain.fixture.MenuFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static kitchenpos.menu.domain.fixture.MenuProductFixture.menuProductA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@DisplayName("메뉴 상품 일급 콜렉션")
class MenuProductsTest {

    @DisplayName("메뉴 상품 일급 콜렉션을 생성한다.")
    @Test
    void create() {
        assertThatNoException().isThrownBy(MenuProducts::new);
    }

    @DisplayName("메뉴 상품 일급 콜렉션의 가격의 합을 구한다.")
    @Test
    void sum() {
        MenuProducts menuProducts = new MenuProducts(Collections.singletonList(menuProductA()));
        assertThat(menuProducts.sum()).isEqualTo(BigDecimal.valueOf(2));
    }

    @DisplayName("메뉴 상품 일급 콜렉션의 empty 여부를 반환한다.")
    @Test
    void isEmpty() {
        MenuProducts menuProducts = new MenuProducts(Collections.singletonList(menuProductA()));
        assertThat(menuProducts.isEmpty()).isEqualTo(false);
    }

    @DisplayName("메뉴와 매핑한다.")
    @Test
    void mapMenu() {
        MenuProducts menuProducts = new MenuProducts(Collections.singletonList(menuProductA()));
        menuProducts.mapMenu(MenuFixture.menuA());
        for (MenuProduct menuProduct : menuProducts.getMenuProducts()) {
            assertThat(menuProduct.getMenu()).isNotNull();
        }
    }
}
