package kitchenpos.domain;

import static kitchenpos.exception.ErrorCode.PRICE_GREATER_THAN_SUM;
import static kitchenpos.exception.ErrorCode.PRICE_IS_NULL_OR_MINUS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.exception.KitchenposException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class MenuTest {
    @Test
    public void 생성() {
        Menu menu = new Menu(1L, "후라이드", BigDecimal.valueOf(16000), 1L);

        assertAll(
                () -> assertThat(menu.getId()).isEqualTo(1L),
                () -> assertThat(menu.getName()).isEqualTo("후라이드"),
                () -> assertThat(menu.getPrice()).isEqualTo(BigDecimal.valueOf(16000)),
                () -> assertThat(menu.getMenuGroupId()).isEqualTo(1L)
        );
    }

    @ParameterizedTest
    @NullSource
    void 가격이_NULL인_경우(BigDecimal price) {
        Menu menu = new Menu(1L, "후라이드", price, null);
        assertThatThrownBy(menu::validatePrice)
                .isInstanceOf(KitchenposException.class)
                .hasMessageContaining(PRICE_IS_NULL_OR_MINUS.getDetail());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -10, -100, -9999})
    void 가격이_마이너스인_경우(int price) {
        Menu menu = new Menu(1L, "후라이드치킨", BigDecimal.valueOf(price), null);

        assertThatThrownBy(menu::validatePrice)
                .isInstanceOf(KitchenposException.class)
                .hasMessageContaining(PRICE_IS_NULL_OR_MINUS.getDetail());
    }

    @Test
    void 가격과_메뉴_항목의_가격_합계와_일치하지_않는_경우() {
        Menu menu = new Menu(1L, "후라이드치킨", BigDecimal.valueOf(16000), null);

        assertThatThrownBy(() -> menu.validatePriceGreaterThanSum(BigDecimal.ZERO))
                .isInstanceOf(KitchenposException.class)
                .hasMessageContaining(PRICE_GREATER_THAN_SUM.getDetail());
    }
}
