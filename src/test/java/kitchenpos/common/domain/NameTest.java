package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("이름 테스트")
class NameTest {

    @DisplayName("이름이 null 이면 예외가 발생한다.")
    @Test
    void createNull() {
        assertThatThrownBy(() -> Name.of(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이름이 빈 문자열이면 예외가 발생한다.")
    @Test
    void createEmptyString() {
        assertThatThrownBy(() -> Name.of(""))
            .isInstanceOf(IllegalArgumentException.class);
    }
}