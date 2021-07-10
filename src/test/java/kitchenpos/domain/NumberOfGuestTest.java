package kitchenpos.domain;

import kitchenpos.common.exception.InvalidChangeNumberOfGuestsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class NumberOfGuestTest {
    @Test
    @DisplayName("게스트 숫자가 0명 이하이면 InvalidChangeNumberOfGuestsException이 발생한다")
    void 게스트_숫자가_0명_이하이면_InvalidChangeNumberOfGuestsException이_발생한다() {
        assertThatExceptionOfType(InvalidChangeNumberOfGuestsException.class)
                .isThrownBy(() -> new NumberOfGuest(-1));
    }
    @Test
    @DisplayName("게스트 숫자가 0명 이상이면 정상이다")
    void 게스트_숫자가_0명_이상이면_정상이다() {
        assertDoesNotThrow(() -> new NumberOfGuest(0));
    }
}