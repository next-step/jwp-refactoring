package kitchenpos.tobe.menus.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;

import kitchenpos.tobe.fixture.MenuGroupFixture;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuGroupTest {

    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    @Test
    void create() {
        // given
        final String name = "대표메뉴";

        // when
        final ThrowableAssert.ThrowingCallable request = () -> MenuGroupFixture.of(name);

        // then
        assertThatNoException().isThrownBy(request);
    }
}
