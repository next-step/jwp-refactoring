package kitchenpos.menu.domain;

import static kitchenpos.exception.KitchenposExceptionMessage.MENU_PRICE_CANNOT_OVER_THAN_PRODUCT_PRICE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.common.Price;
import kitchenpos.exception.KitchenposException;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductsTest {

    private MenuProducts menuProducts;

    @BeforeEach
    void setUp() {
        menuProducts = new MenuProducts();
        MenuProduct 테스트_상품_1 = new MenuProduct(1L, Price.of(BigDecimal.valueOf(3000L)), 3); // 9000
        MenuProduct 테스트_상품_2 = new MenuProduct(2L, Price.of(BigDecimal.valueOf(2000L)), 1); // 2000
        menuProducts.add(Arrays.asList(테스트_상품_1, 테스트_상품_2));
    }


    @DisplayName("내부 원소들의 총 가격보다 주어진 가격이 높은지 검사")
    @Test
    void checkOverPriceTest() {
        // when
        assertThatCode(() -> menuProducts.checkOverPrice(Price.of(BigDecimal.valueOf(10000))))
            .doesNotThrowAnyException();
    }

    @DisplayName("내부 원소들의 총 가격이 주어진 가격보다 낮으면 오류")
    @Test
    void checkOverPriceTestWithWrongPrice() {
        // when
        assertThatThrownBy(() -> menuProducts.checkOverPrice(Price.of(BigDecimal.valueOf(13000))))
            .isInstanceOf(KitchenposException.class)
            .hasMessageContaining(MENU_PRICE_CANNOT_OVER_THAN_PRODUCT_PRICE.getMessage());
    }

    @DisplayName("제네릭을 사용한 변환 테스트")
    @Test
    void convertAllTest() {
        // when
        assertThat(menuProducts.convertAll(menuProduct -> menuProduct.getProductId()))
            .hasSize(2)
            .containsExactly(1L, 2L);
    }

}
