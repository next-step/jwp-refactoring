package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import kitchenpos.domain.Price;
import java.math.BigDecimal;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.MenuProductsFixture;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuProductsTest {

    @DisplayName("메뉴 상품 일급 컬렉션을 생성할 수 있다.")
    @Test
    void create() {
        // given
        final MenuProduct menuProduct1 = MenuProductFixture.of(1L, 16_000L, 1L);
        final MenuProduct menuProduct2 = MenuProductFixture.of(2L, 16_000L, 1L);

        final ThrowableAssert.ThrowingCallable request = () -> MenuProductsFixture.of(
            menuProduct1,
            menuProduct2
        );

        // then
        assertThatNoException().isThrownBy(request);
    }

    @DisplayName("메뉴 상품들의 가격 총 합을 구할 수 있다.")
    @Test
    void calculateTotalPrice() {
        // given
        final MenuProducts menuProducts = MenuProductsFixture.of(
            MenuProductFixture.of(1L, 16_000L, 2L),
            MenuProductFixture.of(2L, 8_000L, 1L)
        );

        // when
        final Price totalPrice = menuProducts.calculateTotalPrice();

        // then
        final Price expected = new Price(BigDecimal.valueOf(40_000L));
        assertThat(totalPrice).isEqualTo(expected);
    }
}
