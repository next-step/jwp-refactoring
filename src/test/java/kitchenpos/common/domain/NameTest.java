package kitchenpos.common.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class NameTest {
    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("이름은 비어있을 수 없다.")
    void validate(String name) {
        // then
        assertThatThrownBy(() -> {
            new Name(name);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
