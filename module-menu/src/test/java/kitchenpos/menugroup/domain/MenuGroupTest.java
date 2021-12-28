package kitchenpos.menugroup.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("메뉴 그룹 테스트")
class MenuGroupTest {

    @DisplayName("메뉴 그룹 생성 성공 테스트")
    @Test
    void instantiate_success() {
        // given
        String name = "추천_메뉴_그룹";

        // when
        MenuGroup menuGroup = MenuGroup.of(name);

        // then
        assertAll(
                () -> assertThat(menuGroup).isNotNull()
                , () -> assertThat(menuGroup.getName()).isEqualTo(name)
        );
    }
}
