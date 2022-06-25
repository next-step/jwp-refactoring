package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class MenuGroupTest {

    @ParameterizedTest(name = "\"{0}\" 일 경우")
    @DisplayName("MenuGroup 생성시 유효성 검사를 체크한다.")
    @NullAndEmptySource
    void createFail(String name) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> new MenuGroup(name));
    }

}
