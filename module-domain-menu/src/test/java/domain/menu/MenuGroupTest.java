package domain.menu;

import domain.menu.exception.InvalidMenuGroupNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuGroupTest {

    @Test
    void create() {
        //when
        MenuGroup actual = MenuGroup.of("양식");

        //then
        assertThat(actual.getName()).isEqualTo("양식");
    }

    @DisplayName("메뉴 그룹의 이름을 지정해야한다.")
    @Test
    void createMenuGroupExceptionIfNameIsNull() {
        //when
        assertThatThrownBy(() -> MenuGroup.of(""))
                .isInstanceOf(InvalidMenuGroupNameException.class); //then
        //when
        assertThatThrownBy(() -> MenuGroup.of(null))
                .isInstanceOf(InvalidMenuGroupNameException.class); //then
    }
}
