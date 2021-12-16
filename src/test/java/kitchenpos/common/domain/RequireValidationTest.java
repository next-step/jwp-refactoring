package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("검증 필수 객체")
class RequireValidationTest {

    @Test
    @DisplayName("생성")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> RequireValidation.from("any"));
    }

    @Test
    @DisplayName("검증 대상은 필수")
    void instance_nullTarget_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> RequireValidation.from(null))
            .withMessage("검증할 객체는 필수입니다.");
    }

    @Test
    @DisplayName("검증하고 가져오기")
    void get() {
        //given
        String target = "any";

        //when
        String actual = RequireValidation.from(target)
            .get(Validator.fake());

        //then
        assertThat(actual).isEqualTo(target);
    }
}
