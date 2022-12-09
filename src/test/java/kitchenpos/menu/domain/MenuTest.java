package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static kitchenpos.menu.domain.Menu.MENU_GROUP_NOT_NULL_EXCEPTION_MESSAGE;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메뉴")
class MenuTest {

    @DisplayName("메뉴")
    @ParameterizedTest
    @ValueSource(strings = {"메뉴그룹 A"})
    void constructor(String name) {
        MenuGroup menuGroup = new MenuGroup(1L, "메뉴 그룹");
        assertThatNoException().isThrownBy(() -> new Menu(name, BigDecimal.ONE, menuGroup.getId()));
    }

    @DisplayName("메뉴 그룹이 없을 경우 메뉴를 생성할 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {"메뉴 A"})
    void constructor_fail_menuGroup(String name) {
        assertThatThrownBy(() -> new Menu(name, BigDecimal.ONE, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MENU_GROUP_NOT_NULL_EXCEPTION_MESSAGE);
    }
}
