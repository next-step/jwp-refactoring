package kitchenpos.menu.domain;

import kitchenpos.menu.domain.fixture.MenuGroupFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;

@DisplayName("메뉴 그룹")
class MenuGroupTest {

    @DisplayName("메뉴 그룹 생성")
    @Test
    void constructor() {
        assertThatNoException().isThrownBy(MenuGroupFixture::menuGroupA);
    }

    @DisplayName("메뉴 그룹 생성 / 이름이 없을 수 없다.")
    @Test
    void name() {
        Assertions.assertThatThrownBy(() -> new MenuGroup(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
