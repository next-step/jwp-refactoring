package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.common.fixture.NameFixture.nameMenuGroupA;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메뉴 그룹")
class MenuGroupTest {

    @DisplayName("메뉴 그룹 생성")
    @Test
    void constructor() {
        assertThatNoException().isThrownBy(() -> new MenuGroup(nameMenuGroupA()));
    }

    @DisplayName("메뉴 그룹 생성 / 이름이 없을 수 없다.")
    @Test
    void name() {
        assertThatThrownBy(() -> new MenuGroup(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
