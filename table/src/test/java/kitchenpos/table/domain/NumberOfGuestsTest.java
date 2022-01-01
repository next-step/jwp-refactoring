package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.exception.KitchenposException;

class NumberOfGuestsTest {

    @DisplayName("0 이하의 고객수로 생성시 에러")
    @Test
    void constructFail() {
        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> new NumberOfGuests(-1))
            .withMessage("0 이상의 고객수만 입력 가능합니다.");
    }
}