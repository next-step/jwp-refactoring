package kitchenpos.menu.domain;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("메뉴 그룹 테스트")
class MenuGroupTest {
    @Test
    void 메뉴_그룹_생성() {
        MenuGroup actual = MenuGroup.of(1L, "두마리메뉴");

        Assertions.assertThat(actual).isNotNull();
    }

    @Test
    void 메뉴_그룹_생성_시_메뉴_그룹의_이름은_필수이다() {
        // given - when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> MenuGroup.of(1L, null);

        // then
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(throwingCallable)
                .withMessage("메뉴그룹명은 필수입니다.");
    }
}
