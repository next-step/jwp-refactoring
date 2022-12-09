package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatNoException;

@DisplayName("메뉴 그룹")
class MenuGroupTest {

    @DisplayName("메뉴 그룹")
    @ParameterizedTest
    @ValueSource(strings = {"메뉴그룹 A"})
    void constructor(String name) {
        assertThatNoException().isThrownBy(() -> new MenuGroup(name));
    }
}
