package kitchenpos.menu.domain.unit;

import static kitchenpos.fixture.MenuGroupFixture.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;

import kitchenpos.menu.domain.*;

@DisplayName("메뉴그룹 관련(단위테스트)")
class MenuGroupTest {

    @DisplayName("메뉴그룹 생성하기")
    @Test
    void createTest() {
        assertThat(MenuGroup.from(메뉴그룹_한마리메뉴.getName())).isInstanceOf(MenuGroup.class);
    }
}
