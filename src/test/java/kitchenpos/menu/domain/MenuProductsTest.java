package kitchenpos.menu.domain;

import kitchenpos.fixture.ProductFixture;
import kitchenpos.product.domain.Product;
import kitchenpos.fixture.MenuProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuProductsTest {

    @DisplayName("메뉴상품목록 생성")
    @Test
    void create() {
        MenuProducts menuProducts = new MenuProducts();

        assertThat(menuProducts).isNotNull();
    }

    @DisplayName("메뉴상품 추가")
    @Test
    void addMenuProduct() {
        Product 후라이드 = ProductFixture.생성("후라이드", new BigDecimal("5000"));
        MenuProduct 후라이드두마리= MenuProductFixture.생성(후라이드,2L);
        MenuProducts menuProducts = new MenuProducts();

        menuProducts.add(후라이드두마리);

        assertThat(menuProducts.getList().contains(후라이드두마리)).isTrue();
    }
}
