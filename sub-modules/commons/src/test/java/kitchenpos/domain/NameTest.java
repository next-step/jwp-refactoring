package kitchenpos.domain;

import kitchenpos.common.domain.Name;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Name Common VO 테스트")
public class NameTest {

    @DisplayName("Name 객체 생성 테스트")
    @Test
    void create() {
        Name name = new Name("한마리치킨");

        assertThat(name.value()).isEqualTo("한마리치킨");
    }

    @DisplayName("공백이거나 null 이면 예외 발생")
    @Test
    void validateException() {
        assertAll(
                () -> assertThatThrownBy(() -> new Name(null)).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> new Name("")).isInstanceOf(IllegalArgumentException.class)
        );
    }
}
