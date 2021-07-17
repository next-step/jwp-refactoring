package common.valueobject;

import common.valueobject.exception.InvalidNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NameTest {

    @Test
    void create() {
        //when
        Name actual = Name.of("이름");

        //then
        assertThat(actual.getValue()).isEqualTo("이름");
    }

    @DisplayName("이름은 지정해야합니다.")
    @Test
    void invalidName() {
        //when
        assertThatThrownBy(() -> Name.of(""))
                .isInstanceOf(InvalidNameException.class); //then
        //when
        assertThatThrownBy(() -> Name.of(null))
                .isInstanceOf(InvalidNameException.class); //then
    }
}
