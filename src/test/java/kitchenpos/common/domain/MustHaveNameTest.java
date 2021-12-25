package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import kitchenpos.exception.InvalidArgumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@DisplayName("필수 이름 테스트")
class MustHaveNameTest {

    @DisplayName("validate 체크")
    @ParameterizedTest
    @NullAndEmptySource
    void validate(String input) {
        assertThatThrownBy(() -> MustHaveName.valueOf(input))
            .isInstanceOf(InvalidArgumentException.class)
            .hasMessage("이름은 필수입니다.");
    }

    @Test
    @DisplayName("동등성 비교")
    void equals() {
        String name = "shinmj";
        assertTrue(MustHaveName.valueOf(name).equals(MustHaveName.valueOf(name)));
        assertFalse(MustHaveName.valueOf(name).equals(MustHaveName.valueOf("shin")));
    }
}