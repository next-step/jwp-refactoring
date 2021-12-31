package kitchenpos.core.menugroup;

import kitchenpos.core.menugroup.domain.MenuGroup;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuGroupTest {

    @ParameterizedTest(name = "이름은 빈값이 될 수 없다. \"{0}\"")
    @NullAndEmptySource
    void nameEmpty(String name) {
        // given, when
        ThrowableAssert.ThrowingCallable createCall = () -> MenuGroup.of(name);
        // then
        assertThatThrownBy(createCall).isInstanceOf(IllegalArgumentException.class);
    }
}
