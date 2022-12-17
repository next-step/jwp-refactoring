package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.common.constant.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("이름 관련 도메인 테스트")
public class NameTest {
    
    @DisplayName("이름을 생성한다.")
    @Test
    void createName() {
        // given
        String actualName = "감자튀김";

        // when
        Name name = Name.from(actualName);

        // then
        assertAll(
                () -> assertThat(name.value()).isEqualTo(actualName),
                () -> assertThat(name).isEqualTo(Name.from(actualName))
        );
    }

    @DisplayName("이름이 null이면 에러가 발생한다.")
    @Test
    void createNameThrowErrorWhenNameIsNull() {
        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> Name.from(null))
                .withMessage(ErrorCode.이름은_비어_있을_수_없음.getErrorMessage());
    }

    @DisplayName("이름이 비어있이면 에러가 발생한다.")
    @Test
    void createNameThrowErrorWhenNameIsEmpty() {
        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> Name.from(""))
                .withMessage(ErrorCode.이름은_비어_있을_수_없음.getErrorMessage());
    }
}
