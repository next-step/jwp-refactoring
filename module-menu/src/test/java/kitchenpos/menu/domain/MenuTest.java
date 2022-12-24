package kitchenpos.menu.domain;

import kitchenpos.menu.domain.fixture.MenuFixture;
import kitchenpos.menu.domain.fixture.MenuGroupFixture;
import kitchenpos.menu.domain.fixture.MenuProductsFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static kitchenpos.menu.domain.Menu.MENU_GROUP_NOT_NULL_EXCEPTION_MESSAGE;
import static kitchenpos.menu.domain.Menu.PRICE_NOT_NULL_EXCEPTION_MESSAGE;

@DisplayName("메뉴")
class MenuTest {

    @DisplayName("메뉴")
    @Test
    void constructor() {
        Assertions.assertThatNoException().isThrownBy(() -> MenuFixture.menuA(1L));
    }

    @DisplayName("이름이 없을 수 없다.")
    @Test
    void name() {
        Assertions.assertThatThrownBy(() -> new Menu(null, new Price(BigDecimal.ONE), MenuGroupFixture.menuGroupA(), new MenuProducts(MenuProductsFixture.menuProducts(1L))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 그룹이 없을 경우 메뉴를 생성할 수 없다.")
    @Test
    void constructor_fail_menuGroup() {
        Assertions.assertThatThrownBy(() -> new Menu(new Name("A"), new Price(BigDecimal.ONE), null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MENU_GROUP_NOT_NULL_EXCEPTION_MESSAGE);
    }

    @DisplayName("가격이 없을 경우 메뉴를 생성할 수 없다.")
    @Test
    void constructor_fail_price() {
        Assertions.assertThatThrownBy(() -> new Menu(new Name("A"), null, MenuGroupFixture.menuGroupA(), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(PRICE_NOT_NULL_EXCEPTION_MESSAGE);
    }

    @DisplayName("가격이 0원보다 작을 경우 메뉴를 생성할 수 없다.")
    @Test
    void constructor_fail_price_negative() {
        Assertions.assertThatThrownBy(() -> new Menu(new Name("A"), new Price(BigDecimal.valueOf(-1)), MenuGroupFixture.menuGroupA(), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(Price.PRICE_MINIMUM_EXCEPTION_MESSAGE);
    }
}
