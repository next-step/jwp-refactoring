package kitchenpos.menu.domain;

import kitchenpos.common.Money;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 상품들")
class MenuProductsTest {

    private MenuProduct 강정치킨메뉴상품;
    private MenuProduct 후라이드메뉴상품;

    @BeforeEach
    void setUp() {
        Product 강정치킨 = new Product("강정치킨", Money.valueOf(19000));
        Product 후라이드 = new Product("후라이드", Money.valueOf(15000));
        강정치킨메뉴상품 = new MenuProduct(강정치킨, 2);
        후라이드메뉴상품 = new MenuProduct(후라이드, 2);
    }

    @DisplayName("메뉴 상품들을 생성한다.")
    @Test
    void create() {
        // when
        MenuProducts menuProducts = new MenuProducts(Arrays.asList(강정치킨메뉴상품, 후라이드메뉴상품), null);

        // then
        assertThat(menuProducts).isNotNull();
    }

    @DisplayName("메뉴 상품들의 가격 총합을 구한다.")
    @Test
    void price() {
        // given
        MenuProducts menuProducts = new MenuProducts(Arrays.asList(강정치킨메뉴상품, 후라이드메뉴상품), null);

        // when
        Money price = menuProducts.price();

        // then
        assertThat(price).isEqualTo(강정치킨메뉴상품.price().add(후라이드메뉴상품.price()));
    }
}
