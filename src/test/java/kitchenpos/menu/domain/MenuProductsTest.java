package kitchenpos.menu.domain;

import static kitchenpos.product.domain.ProductTestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.common.exception.InvalidParameterException;
import kitchenpos.common.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductsTest {
    @Test
    @DisplayName("생성")
    void createMenuProducts() {
        // when
        MenuProducts actual = MenuProducts.from(Collections.singletonList(MenuProduct.of(짜장면, 1L)));

        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isInstanceOf(MenuProducts.class)
        );
    }

    @Test
    @DisplayName("메뉴 상품 리스트 필수")
    void createMenuProductsByNull() {
        // when & then
        assertThatThrownBy(() -> MenuProducts.from(null))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("메뉴 상품 목록은 필수입니다.");
    }
    
    @Test
    @DisplayName("메뉴 상품 가격 합계 구하기")
    void menuProductPrice() {
        // given
        MenuProducts menuProducts = MenuProducts.from(
                Arrays.asList(MenuProduct.of(짜장면, 1L), MenuProduct.of(짬뽕, 2L)));

        // when & then
        assertThat(menuProducts.totalPrice()).isEqualTo(Price.from(BigDecimal.valueOf(23_000L)));
    }
}
