package kitchenpos.menu.domain;

import kitchenpos.domain.Price;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MenuProductsTest {

    List<MenuProduct> menuProductItems;
    MenuProducts menuProducts;

    @BeforeEach
    void setUp() {
        menuProductItems = Arrays.asList(
                MenuProduct.of(ProductFixture.후라이드, 1L),
                MenuProduct.of(ProductFixture.강정치킨, 1L)
        );
        menuProducts = new MenuProducts(menuProductItems);
    }

    @Test
    @DisplayName("메뉴 상품 목록 생성에 성공한다")
    void createMenuProductsTest() {
        // when
        MenuProducts menuProducts = new MenuProducts(menuProductItems);

        // then
        assertThat(menuProducts).isEqualTo(new MenuProducts(menuProductItems));
    }

    @Test
    @DisplayName("상품 목록의 총 가격을 반환한다")
    void getTotalPriceTest() {
        // when
        Price totalPrice = menuProducts.totalPrice();

        // then
        assertThat(totalPrice).isEqualTo(Price.of(28_000L));
    }
}
