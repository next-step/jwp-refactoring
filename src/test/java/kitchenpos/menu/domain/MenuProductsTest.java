package kitchenpos.menu.domain;

import static kitchenpos.common.exception.ExceptionMessage.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import kitchenpos.common.domain.Price;
import kitchenpos.common.exception.BadRequestException;

class MenuProductsTest {

    private MenuProducts menuProducts;

    @BeforeEach
    void setUp() {
        menuProducts = new MenuProducts(Arrays.asList(
            MenuProduct.of(null, new Product("후라이드", new BigDecimal(10000)), 2),
            MenuProduct.of(null, new Product("양념치킨", new BigDecimal(10000)), 1)
        ));
    }

    @DisplayName("메뉴가격은 상품의 총 금액보다 작거나 같아야 한다.")
    @ParameterizedTest
    @ValueSource(ints = {25000, 30000})
    void compareMenuPriceToProductSumPrice(int price) {
        // given
        Price menuPrice = new Price(new BigDecimal(price));

        // when && then
        assertDoesNotThrow(() -> menuProducts.compareMenuPriceToProductSumPrice(menuPrice));
    }

    @DisplayName("메뉴가격은 상품의 총 금액보다 클 수 없다.")
    @Test
    void compareMenuPriceToProductSumPriceException() {
        // given
        Price menuPrice = new Price(new BigDecimal(30001));

        // when && then
        assertThatThrownBy(() -> menuProducts.compareMenuPriceToProductSumPrice(menuPrice))
            .isInstanceOf(BadRequestException.class)
            .hasMessage(WRONG_VALUE.getMessage());
    }
}
