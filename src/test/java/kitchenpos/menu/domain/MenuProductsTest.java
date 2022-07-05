package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.testfixture.CommonTestFixture.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MenuProductsTest {
    @Test
    @DisplayName("상품이 존재해야 한다.")
    void validateEmpty() {
        // then
        assertThatThrownBy(() -> {
            new MenuProducts(Collections.emptyList(), null, new Price(BigDecimal.valueOf(37_500)));
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 금액의 합은 메뉴 가격보다 작아야 한다.")
    void validateMenuPrice() {
        // given
        Product 후라이드싸이순살 = createProduct("후라이드싸이순살", BigDecimal.valueOf(20_000));
        Product 블랙쏘이치킨 = createProduct("블랙쏘이치킨", BigDecimal.valueOf(18_000));
        List<MenuProduct> menuProducts = Arrays.asList(
                new MenuProduct(후라이드싸이순살.getId(), 1L),
                new MenuProduct(블랙쏘이치킨.getId(), 1L)
        );

        // then
        assertThatThrownBy(() -> {
            new MenuProducts(menuProducts, null, new Price(BigDecimal.valueOf(37_500)));
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
