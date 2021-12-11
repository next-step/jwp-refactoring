package kitchenpos.menu.domain.menugroup;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static kitchenpos.fixture.MenuGroupDomainFixture.menuGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메뉴 그룹 관리")
class MenuGroupTest {

    @Nested
    @DisplayName("메뉴 그룹 생성")
    class CreateMenuGroup {

        @Test
        @DisplayName("성공")
        public void create() {
            // given
            String expected = "치킨 세트";

            // when
            MenuGroup actual = menuGroup(expected);

            // then
            assertThat(actual.getName()).isEqualTo(expected);
        }

        @Test
        @DisplayName("실패 - 그룹 메뉴명 없음.")
        public void fail() {
            // given
            String expected = "";

            // when
            assertThatThrownBy(() -> {
                MenuGroup actual = menuGroup(expected);
            }).isInstanceOf(IllegalArgumentException.class);
        }

    }
}
