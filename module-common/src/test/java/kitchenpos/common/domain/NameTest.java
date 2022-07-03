package kitchenpos.common.domain;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ExceptionType;
import kitchenpos.domain.Name;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("이름 객체에 대한 단위 테스트")
class NameTest {

    @DisplayName("이름이 null 이면 예외가 발생한다")
    @Test
    void exception_test() {
        String value = null;

        assertThatThrownBy(() -> {
            new Name(value);
        }).isInstanceOf(BadRequestException.class)
                .hasMessageContaining(ExceptionType.INVALID_NAME.getMessage());
    }

    @DisplayName("이름이 공백이면 예외가 발생한다")
    @Test
    void exception_test2() {
        String value = "";

        assertThatThrownBy(() -> {
            new Name(value);
        }).isInstanceOf(BadRequestException.class)
                .hasMessageContaining(ExceptionType.INVALID_NAME.getMessage());
    }
}
