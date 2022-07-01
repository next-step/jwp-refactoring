package kitchenpos.menugroup.domain;

import kitchenpos.common.domain.Name;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuGroupTest {

    @Test
    @DisplayName("메뉴 그룹 생성")
    void menuGroup() {
        // given
        Name name = Name.of("피자_2판_메뉴_그룹");

        // when
        MenuGroup 피자_2판_메뉴_그룹 = MenuGroup.of(name);

        // then
        assertAll(
                () -> assertThat(피자_2판_메뉴_그룹).isNotNull(),
                () -> assertThat(피자_2판_메뉴_그룹.getName()).isEqualTo(name.getName())
        );
    }
}
