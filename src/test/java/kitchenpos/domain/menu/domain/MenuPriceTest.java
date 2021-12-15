package kitchenpos.domain.menu.domain;

import kitchenpos.domain.menu.domain.MenuPrice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * - 메뉴의 가격은 0원 이상이어야한다.
 * - 메뉴의 가격은 메뉴 상품의 가격의 합보다 작아야 한다.
 */
class MenuPriceTest {

    @ParameterizedTest
    @ValueSource(strings = {"-1","-10"})
    @DisplayName("메뉴의 가격은 0원 이상이어야한다.")
    void create(BigDecimal 올바르지_않은_가격) {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new MenuPrice(올바르지_않은_가격));
    }

    @ParameterizedTest
    @CsvSource({"10,8"})
    @DisplayName("메뉴의 가격은 메뉴 상품의 가격의 합보다 작아야 한다.")
    void checkLessThan(BigDecimal 메뉴가격, BigDecimal 메뉴상품_가격_합) {
        MenuPrice menuPrice = new MenuPrice(메뉴가격);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> menuPrice.checkLessThan(메뉴상품_가격_합));
    }
}
