package kitchenpos.menu.domain;

import kitchenpos.common.Name;
import kitchenpos.common.Price;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Arrays;

import static kitchenpos.common.Price.PRICE_MINIMUM_EXCEPTION_MESSAGE;
import static kitchenpos.menu.domain.Menu.*;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메뉴")
class MenuTest {

    @DisplayName("메뉴")
    @ParameterizedTest
    @ValueSource(strings = {"메뉴그룹 A"})
    void constructor(String name) {
        assertThatNoException().isThrownBy(() -> new Menu(new Name(name), new Price(BigDecimal.ONE), MenuGroupFixture.menuGroup(), Arrays.asList(new MenuProduct(null, new Product(new Name("A"), new Price(BigDecimal.ONE)), 1L))));
    }

    @DisplayName("메뉴 그룹이 없을 경우 메뉴를 생성할 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {"메뉴 A"})
    void constructor_fail_menuGroup(String name) {
        assertThatThrownBy(() -> new Menu(new Name(name), new Price(BigDecimal.ONE), null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MENU_GROUP_NOT_NULL_EXCEPTION_MESSAGE);
    }

    @DisplayName("가격이 없을 경우 메뉴를 생성할 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {"메뉴 A"})
    void constructor_fail_price(String name) {
        assertThatThrownBy(() -> new Menu(new Name(name), null, new MenuGroup(1L, "메뉴그룹"), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(PRICE_NOT_NULL_EXCEPTION_MESSAGE);
    }

    @DisplayName("가격이 0원보다 작을 경우 메뉴를 생성할 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {"메뉴 A"})
    void constructor_fail_price_negative(String name) {
        assertThatThrownBy(() -> new Menu(new Name(name), new Price(BigDecimal.valueOf(-1)), new MenuGroup(1L, "메뉴그룹"), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(PRICE_MINIMUM_EXCEPTION_MESSAGE);
    }
}
