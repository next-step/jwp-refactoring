package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import kitchenpos.common.exception.InvalidParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupTest {
    @Test
    @DisplayName("메뉴 그룹 생성")
    void createMenuGroup() {
        // when
        MenuGroup actual = MenuGroup.from("면류");

        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isInstanceOf(MenuGroup.class)
        );
    }

    @Test
    @DisplayName("메뉴 그룹명 필수")
    void createMenuGroupByNameIsNotNull() {
        // when & then
        assertThatThrownBy(() -> MenuGroup.from(null))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("이름은 필수입니다.");
    }
}
