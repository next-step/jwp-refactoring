package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NameTest {

    @Test
    @DisplayName("정상 생성")
    void create() {
        // when
        String value = "홍길동";
        Name name = Name.from(value);

        // then
        assertThat(name).isNotNull();
        assertThat(name.value()).isEqualTo(value);
    }

    @Test
    @DisplayName("null을 파라미터로 생성하면 예외 발생")
    void create_null() {
        // expect
        assertThatThrownBy(() -> Name.from(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 값을 파라미터로 생성하면 예외 발생")
    void create_empty() {
        // expect
        assertThatThrownBy(() -> Name.from(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

}