package kitchenpos.menu.domain;

import static kitchenpos.product.domain.ProductTestFixture.짬뽕;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import kitchenpos.common.exception.InvalidParameterException;
import kitchenpos.common.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MenuProductTest {
    @Test
    @DisplayName("메뉴 상품 객체 생성")
    void createMenuProduct() {
        // when
        MenuProduct actual = MenuProduct.of(짬뽕.id(), 1L);

        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isInstanceOf(MenuProduct.class)
        );
    }

    @Test
    @DisplayName("메뉴 상품 객체 생성시 상품은 필수")
    void createMenuProductByProductIsNull() {
        // when & then
        assertThatThrownBy(() -> MenuProduct.of(null, 1L))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("상품은 필수입니다.");
    }

    @ParameterizedTest(name = "[{index} 메뉴 상품 객체 생성시 수량은 0이상]")
    @ValueSource(longs = {-1, Long.MIN_VALUE})
    void createMenuProductByQuantityGreaterThanZero(long quantity) {
        // when & then
        assertThatThrownBy(() -> MenuProduct.of(짬뽕.id(), quantity))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("수량은 0 이상이어야 합니다.");
    }

    @Test
    @DisplayName("메뉴 상품 금액 구하기")
    void menuProductPrice() {
        // given
        MenuProduct menuProduct = MenuProduct.of(짬뽕.id(), 2L);

        // when
        Price actual = menuProduct.price(짬뽕.price());

        // then
        assertThat(actual.value()).isEqualTo(BigDecimal.valueOf(16_000L));
    }
}
