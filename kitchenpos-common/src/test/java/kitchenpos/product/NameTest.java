package kitchenpos.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import kitchenpos.common.Name;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class NameTest {

    @DisplayName("이름 생성")
    @Test
    void 이름_생성() {
        // when
        Name 이름 = Name.of("상품");

        // then
        assertThat(이름).isEqualTo(Name.of("상품"));
    }

    @DisplayName("이름이 null인 경우 예외 발생")
    @Test
    void 이름_생성_예외() {
        assertThatIllegalArgumentException().isThrownBy(
            () -> Name.of(null)
        );
    }

    @DisplayName("이름이 공백인 경우 예외 발생")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    void 이름_생성_예외(String value) {
        assertThatIllegalArgumentException().isThrownBy(
            () -> Name.of(value)
        );
    }


}