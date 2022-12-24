package kitchenpos.menu.domain;

import kitchenpos.common.vo.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static java.util.Collections.singletonList;
import static kitchenpos.common.fixture.NameFixture.nameMenuA;
import static kitchenpos.common.fixture.PriceFixture.priceMenuA;
import static kitchenpos.common.vo.Price.PRICE_MINIMUM_EXCEPTION_MESSAGE;
import static kitchenpos.menu.domain.Menu.MENU_GROUP_NOT_NULL_EXCEPTION_MESSAGE;
import static kitchenpos.menu.domain.Menu.PRICE_NOT_NULL_EXCEPTION_MESSAGE;
import static kitchenpos.menu.domain.fixture.MenuFixture.menuA;
import static kitchenpos.menu.domain.fixture.MenuGroupFixture.menuGroupA;
import static kitchenpos.menu.domain.fixture.MenuProductFixture.menuProductA;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        Assertions.assertThatThrownBy(() -> new Menu(null, PriceFixture.priceMenuA(), MenuGroupFixture.menuGroupA(), new MenuProducts(Collections.singletonList(MenuProductFixture.menuProductA(1L)))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 그룹이 없을 경우 메뉴를 생성할 수 없다.")
    @Test
    void constructor_fail_menuGroup() {
        Assertions.assertThatThrownBy(() -> new Menu(NameFixture.nameMenuA(), PriceFixture.priceMenuA(), null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MENU_GROUP_NOT_NULL_EXCEPTION_MESSAGE);
    }

    @DisplayName("가격이 없을 경우 메뉴를 생성할 수 없다.")
    @Test
    void constructor_fail_price() {
        Assertions.assertThatThrownBy(() -> new Menu(NameFixture.nameMenuA(), null, MenuGroupFixture.menuGroupA(), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(PRICE_NOT_NULL_EXCEPTION_MESSAGE);
    }

    @DisplayName("가격이 0원보다 작을 경우 메뉴를 생성할 수 없다.")
    @Test
    void constructor_fail_price_negative() {
        Assertions.assertThatThrownBy(() -> new Menu(NameFixture.nameMenuA(), new Price(BigDecimal.valueOf(-1)), MenuGroupFixture.menuGroupA(), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(Price.PRICE_MINIMUM_EXCEPTION_MESSAGE);
    }
}
