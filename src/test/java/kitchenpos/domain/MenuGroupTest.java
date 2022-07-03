package kitchenpos.domain;


import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.menugroup.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupTest {


    @Test
    @DisplayName("메뉴 그룹의 이름은 존재 하여야 합니다.")
    void menuGroupNameValidationTest() {
        assertAll(
                () -> assertThatIllegalArgumentException().isThrownBy(() -> new MenuGroup("")),
                () -> assertThatIllegalArgumentException().isThrownBy(() -> new MenuGroup(null))
        );
    }
}
