package kitchenpos.menu.domain;

import static kitchenpos.product.domain.ProductTestFixture.짜장면;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Collections;
import kitchenpos.common.exception.InvalidParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductsTest {
    @Test
    @DisplayName("생성")
    void createMenuProducts() {
        // when
        MenuProducts actual = MenuProducts.from(Collections.singletonList(MenuProduct.of(짜장면.id(), 1L)));

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
}
