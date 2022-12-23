package kitchenpos.menu;

import kitchenpos.product.domain.MenuProduct;
import kitchenpos.product.domain.MenuProducts;
import kitchenpos.common.Quantity;
import kitchenpos.common.Price;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MenuProductsTest {

    @DisplayName("빈 메뉴상품 생성불가 테스트")
    @Test
    void emptyMenuProductsTest() {
        assertThatThrownBy(() -> new MenuProducts(Arrays.asList()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴상품의 총 가격합 확인 테스트")
    @Test
    void getProductPriceSumTest() {
        //given
        int product1PriceValue = 3000;
        int product1Quantity = 3;
        Product product1 = new Product("김밥", new Price(new BigDecimal(product1PriceValue)));
        final MenuProduct menuProduct1 = new MenuProduct(1L, 1L, product1, new Quantity(product1Quantity));

        int product2PriceValue = 5000;
        int product2Quantity = 4;
        Product product2 = new Product("라볶이", new Price(new BigDecimal(product2PriceValue)));
        final MenuProduct menuProduct2 = new MenuProduct(1L, 1L, product2, new Quantity(product2Quantity));

        int totalProductPriceSum = product1PriceValue * product1Quantity + product2PriceValue * product2Quantity;

        //when
        final MenuProducts menuProducts = new MenuProducts(Arrays.asList(menuProduct1, menuProduct2));

        //then
        assertThat(new Price(new BigDecimal(totalProductPriceSum)))
                .isEqualTo(menuProducts.getProductPriceSum());
    }
}
