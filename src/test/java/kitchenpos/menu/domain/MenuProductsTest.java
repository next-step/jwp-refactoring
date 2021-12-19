package kitchenpos.menu.domain;

import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuGroupTestFixture;
import kitchenpos.fixture.MenuProductTextFixture;
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
        Product 후라이드 = new Product();
        후라이드.setId(1L);
        후라이드.setPrice(new BigDecimal("5000"));
        MenuProducts menuProducts = new MenuProducts();
        menuProducts.add(MenuProductTextFixture.생성(1L,후라이드,2L));

        assertThat(menuProducts.size()).isEqualTo(1);
    }
}
