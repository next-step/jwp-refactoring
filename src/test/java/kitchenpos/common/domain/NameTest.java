package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("이름")
class NameTest {

    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 생성 가능")
    @DisplayName("생성")
    @ValueSource(strings = {"name", "이름"})
    void instance(String value) {
        assertThatNoException()
            .isThrownBy(() -> Name.from(value));
    }

    @NullAndEmptySource
    @DisplayName("이름 텍스트는 필수")
    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 으로 생성 불가능")
    void instance_emptyValue_thrownIllegalArgumentException(String value) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Name.from(value))
            .withMessage("이름의 텍스트는 필수입니다.");
    }
}
