package kitchenpos.menu.domain;

import kitchenpos.fixture.MenuGroupTestFixture;
import kitchenpos.menuGroup.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuProductsTest {

    @DisplayName("메뉴상품목록 생성")
    @Test
    void create() {
        MenuProducts menuProducts = MenuProducts.empty();

        assertThat(menuProducts).isNotNull();
    }

    @DisplayName("메뉴상품 추가")
    @Test
    void addMenuProduct() {
        MenuProducts menuProducts = new MenuProducts();
        menuProducts.add(new MenuProduct(1L, 1L, 2L));

        assertThat(menuProducts.size()).isEqualTo(1);
    }
}
