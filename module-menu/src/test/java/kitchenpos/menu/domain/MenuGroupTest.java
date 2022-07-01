package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ExceptionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 그룹에 대한 단위 테스트")
class MenuGroupTest {

    @DisplayName("메뉴 그룹의 이름이 있으면 정상적으로 생성된다")
    @Test
    void create_test() {
        // when
        MenuGroup menuGroup = MenuGroup.of("치킨");

        // then
        assertThat(menuGroup.getName()).isEqualTo("치킨");
    }

    @DisplayName("메뉴 그룹의 이름이 null 이면 예외가 발생한다")
    @Test
    void exception_test() {
        assertThatThrownBy(() -> {
            MenuGroup.of(null);
        }).isInstanceOf(BadRequestException.class)
            .hasMessageContaining(ExceptionType.INVALID_NAME.getMessage());
    }

    @DisplayName("메뉴 그룹의 이름이 공백이면 예외가 발생한다")
    @Test
    void exception_test2() {
        assertThatThrownBy(() -> {
            MenuGroup.of("");
        }).isInstanceOf(BadRequestException.class)
            .hasMessageContaining(ExceptionType.INVALID_NAME.getMessage());
    }
}
