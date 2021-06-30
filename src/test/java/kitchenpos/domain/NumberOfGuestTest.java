package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;

class NumberOfGuestTest {
    @Test
    @DisplayName("게스트 숫자가 0명 이하이면 IllegalArgumentException이 발생한다")
    void 게스트_숫자가_0명_이하이면_IllegalArgumentException이_발생한다() {
        assertThatIllegalArgumentException().isThrownBy(() -> new NumberOfGuest(-1L));
    }
    @Test
    @DisplayName("게스트 숫자가 0명 이상이면 정상이다")
    void 게스트_숫자가_0명_이상이면_정상이다() {
        assertDoesNotThrow(() -> new NumberOfGuest(0L));
    }
}