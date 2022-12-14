package kitchenpos.menu.domain;

import kitchenpos.exception.ExceptionMessage;
import kitchenpos.exception.InvalidNameSizeException;
import kitchenpos.menu.domain.MenuName;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("메뉴 이름 테스트")
class MenuNameTest {

    @DisplayName("메뉴 이름이 null 이거나 empty 이면 예외를 발생한다.")
    @ParameterizedTest(name = "{index} | {displayName} | {argumentsWithNames}")
    @NullAndEmptySource
    void nullOrEmpty(String input) {
        Assertions.assertThatThrownBy(() -> MenuName.from(input))
                .isInstanceOf(InvalidNameSizeException.class)
                .hasMessageStartingWith(ExceptionMessage.INVALID_MENU_NAME_SIZE);
    }

    @DisplayName("메뉴 이름이 null 과 empty가 아니면 정상적으로 생성된다.")
    @ParameterizedTest(name = "{index} | {displayName} | {argumentsWithNames}")
    @ValueSource(strings = {"버팔로윙", "치킨텐더", "국"})
    void create(String input) {
        MenuName result = MenuName.from(input);

        Assertions.assertThat(result).isNotNull();
    }

    @DisplayName("메뉴 이름이 다르면 메뉴 이름 객체는 동등하지 않다.")
    @Test
    void equalsTest() {
        MenuName menuName1 = MenuName.from("버팔로윙");
        MenuName menuName2 = MenuName.from("치킨텐더");

        Assertions.assertThat(menuName1).isNotEqualTo(menuName2);
    }

    @DisplayName("메뉴 이름이 같으면 메뉴 이름 객체는 동등하다.")
    @Test
    void equalsTest2() {
        MenuName menuName1 = MenuName.from("버팔로윙");
        MenuName menuName2 = MenuName.from("버팔로윙");

        Assertions.assertThat(menuName1).isEqualTo(menuName2);
    }
}
