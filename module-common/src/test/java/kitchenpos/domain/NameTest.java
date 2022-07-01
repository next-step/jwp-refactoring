package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NameTest {
    @DisplayName("초기화 테스트")
    @Test
    void from() {
        Name name = Name.from("name");
        assertThat(name.value()).isEqualTo("name");
    }

    @DisplayName("null 경우 테스트")
    @Test
    void ofWithNull() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Name.from(null))
                .withMessage("이름을 지정해야 합니다.");
    }
}
