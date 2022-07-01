package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupTest {

    @Test
    @DisplayName("MenuGroup이름을 기입하지 않거나, 공백으로 설정시 에러 발생")
    public void createMenuGroupWithInvalidName() {
        assertAll(
            () -> assertThatThrownBy(() -> new MenuGroup(""))
                .isInstanceOf(IllegalArgumentException.class),
            () -> assertThatThrownBy(() -> new MenuGroup(null))
                .isInstanceOf(IllegalArgumentException.class)
        );
    }

}