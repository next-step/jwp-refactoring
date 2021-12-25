package kitchenpos.tobe.menus.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.math.BigDecimal;
import kitchenpos.tobe.common.domain.Price;
import kitchenpos.tobe.fixture.MenuProductFixture;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuProductTest {

    @DisplayName("메뉴 상품을 생성할 수 있다.")
    @Test
    void create() {
        // given
        final Long productId = 1L;
        final long price = 16_000L;
        final long quantity = 1L;

        final ThrowableAssert.ThrowingCallable request = () -> MenuProductFixture.of(
            productId,
            price,
            quantity
        );

        // then
        assertThatNoException().isThrownBy(request);
    }

    @DisplayName("메뉴 상품 수량을 고려한 메뉴 상품 가격의 총 합을 구할 수 있다.")
    @Test
    void calculateTotalPrice() {
        // given
        final MenuProduct menuProduct = MenuProductFixture.of(1L, 16_000L, 2);

        // when
        final Price totalPrice = menuProduct.calculateTotalPrice();

        // then
        final Price expected = new Price(BigDecimal.valueOf(32_000L));
        assertThat(totalPrice).isEqualTo(expected);
    }
}
