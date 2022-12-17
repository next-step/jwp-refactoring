package kitchenpos.menu.domain;

import kitchenpos.exception.MenuGroupErrorMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("메뉴 그룹 테스트")
public class MenuGroupTest {

    @DisplayName("이름이 동일하면 메뉴 그룹은 동일하다.")
    @Test
    void 이름이_동일하면_메뉴_그룹은_동일하다() {
        assertEquals(
                new MenuGroup("메뉴 그룹"),
                new MenuGroup("메뉴 그룹")
        );
    }

    @DisplayName("이름이 null이거나 빈값이면 메뉴 그룹을 생성할 수 없다.")
    @ParameterizedTest
    @NullAndEmptySource
    void 이름이_null이거나_빈값이면_메뉴_그룹을_생성할_수_없다(String name) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new MenuGroup(name))
                .withMessage(MenuGroupErrorMessage.REQUIRED_NAME.getMessage());
    }
}
