package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class NameTest {

    @DisplayName("이름은 생성할 수 있다.")
    @Test
    void name() {
        //given
        String name = "이름";
        //when
        Name actual = Name.from(name);
        //then
        Assertions.assertThat(actual.value()).isEqualTo(name);
    }

    @DisplayName("이름의 값이 없으면 에러가 발생한다.")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {" ", ""})
    void nameValid(String name) {
        assertThatThrownBy(() -> Name.from(name)).isInstanceOf(IllegalArgumentException.class);
    }
}
