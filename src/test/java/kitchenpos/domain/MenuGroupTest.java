package kitchenpos.domain;

import static kitchenpos.domain.MenuGroupTestFixture.generateMenuGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.common.constant.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 그룹 관련 도메인 테스트")
public class MenuGroupTest {

    @DisplayName("메뉴 그릅 생성 시, 이름이 null이면 에러가 발생한다.")
    @Test
    void createMenuGroupThrowErrorWhenNameIsNull() {
        // when & then
        assertThatThrownBy(() -> generateMenuGroup(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.이름은_비어_있을_수_없음.getErrorMessage());
    }

    @DisplayName("메뉴 그릅 생성 시, 이름이 비어있이면 에러가 발생한다.")
    @Test
    void createMenuGroupThrowErrorWhenNameIsEmpty() {
        // when & then
        assertThatThrownBy(() -> generateMenuGroup(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.이름은_비어_있을_수_없음.getErrorMessage());
    }

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void createMenuGroup() {
        // given
        String name = "햄버거단품";

        // when
        MenuGroup menuGroup = generateMenuGroup(name);

        // then
        assertThat(menuGroup.getName()).isEqualTo(Name.from(name));
    }
}
