package kitchenpos.common.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("이름 테스트")
class NameTest {

    @DisplayName("이름 생성 성공 테스트")
    @Test
    void instantiate_success() {
        // given
        String name = "test";

        // when & then
        assertThat(Name.of(name)).isExactlyInstanceOf(Name.class);
    }

    @DisplayName("이름 생성 실패 테스트 - 공백")
    @Test
    void instantiate_failure_empty() {
        // given
        String name = "";

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Name.of(name));
    }

    @DisplayName("이름 생성 실패 테스트 - null")
    @Test
    void instantiate_failure_null() {
        // given
        String name = null;

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Name.of(name));
    }
}
