package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class NameTest {
    @Test
    @DisplayName("null이면 생성이 불가능하다")
    public void null이면_생성이_불가능하다() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Name(null));
    }
}