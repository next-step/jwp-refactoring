package kitchenpos.domain;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuProductsTest {

    @DisplayName("메뉴상품의 총 가격")
    @Test
    void 메뉴상품의_총_가격() {
        //given
        MenuProducts menuProducts = new MenuProducts(
                Arrays.asList(
                        new MenuProduct(
                                new Product("후라이드치킨", BigDecimal.valueOf(18000L)),
                                2L
                        ),
                        new MenuProduct(
                                new Product("양념치킨", BigDecimal.valueOf(19000L)),
                                1L
                        )
                )
        );

        assertThat(menuProducts.sumOfMenuProducts().longValue()).isEqualTo(55000L);
    }
}
