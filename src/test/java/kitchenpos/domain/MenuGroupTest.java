package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupTest {
    @Test
    @DisplayName("name이 null일 경우 Exception")
    void nameIsNullException() {
        assertThatThrownBy(() -> new MenuGroup(null))
                .isInstanceOf(RuntimeException.class);
    }
}
