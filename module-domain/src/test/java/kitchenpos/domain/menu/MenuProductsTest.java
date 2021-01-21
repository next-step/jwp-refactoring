package kitchenpos.domain.menu;

import kitchenpos.common.Price;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class MenuProductsTest {
    Product 후라이드;
    Product 양념치킨;
    MenuProduct menuProduct1;
    MenuProduct menuProduct2;
    MenuProducts menuProducts;

    @BeforeEach
    void setUp() {
        후라이드 = new Product("후라이드", Price.of(16000));
        양념치킨 = new Product("양념치킨", Price.of(16000));
        menuProduct1 = new MenuProduct(후라이드, 2L);
        menuProduct2 = new MenuProduct(양념치킨, 1L);
        menuProducts = new MenuProducts(Arrays.asList(menuProduct1, menuProduct2));
    }

    @Test
    @DisplayName("모든 메뉴 가격의 합을 구할 수 있다.")
    public void calculateSumPrice() throws Exception {
        // when
        Price price = menuProducts.calculateSumPrice();

        // then
        assertThat(price).isNotNull();
        assertThat(price).isEqualTo(menuProduct1.calculatePrice().add(menuProduct2.calculatePrice()));
    }
}
