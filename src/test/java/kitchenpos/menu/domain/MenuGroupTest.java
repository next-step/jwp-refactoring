package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.common.NameFixture.nameMenuGroupA;
import static org.assertj.core.api.Assertions.assertThatNoException;

@DisplayName("메뉴 그룹")
class MenuGroupTest {

    @DisplayName("메뉴 그룹")
    @Test
    void constructor() {
        assertThatNoException().isThrownBy(() -> new MenuGroup(nameMenuGroupA()));
    }
}
