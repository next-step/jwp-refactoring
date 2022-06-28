package kitchenpos.domain.core;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import kitchenpos.domain.common.NumberOfGuests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NumberOfGuestsTest {

    @Test
    @DisplayName("NumberOfGuests 생성시 유효성 검사를 진행한다.")
    void createFail() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> new NumberOfGuests(-1));
    }

}
