package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatNoException;

@DisplayName("메뉴")
class MenuTest {

    @DisplayName("메뉴")
    @ParameterizedTest
    @ValueSource(strings = {"메뉴그룹 A"})
    void constructor(String name) {
        MenuGroup menuGroup = new MenuGroup(1L, "메뉴 그룹");
        assertThatNoException().isThrownBy(() -> new Menu(name, BigDecimal.ONE, menuGroup.getId()));
    }
}
