package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.menu.domain.Name;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NameTest {
    @Test
    @DisplayName("이름이 null 이거나 빈값일 경우 - 오류")
    void validateName() {
        assertAll(
            () -> assertThatThrownBy(() -> assertThat(new kitchenpos.menu.domain.Name(null))
                .isInstanceOf(IllegalArgumentException.class)),
            () -> assertThatThrownBy(() -> assertThat(new Name(""))
                .isInstanceOf(IllegalArgumentException.class))
        );
    }
}
