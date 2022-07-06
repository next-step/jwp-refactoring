package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupTest {

    @Test
    @DisplayName("메뉴 그룹의 이름은 공백이면 안됩니다.")
    void nameIsNotEmpty() {
        String name = "";

        assertThatIllegalArgumentException().isThrownBy(
                () -> MenuGroup.from(name)
        );
    }

}