package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import kitchenpos.common.exception.InvalidParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NameTest {
    @Test
    @DisplayName("이름 생성")
    void createName() {
        // when
        Name actual = Name.from("짬뽕");

        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isInstanceOf(Name.class)
        );
    }

    @Test
    @DisplayName("이름은 비어있을 수 없다.")
    void createNameByNull() {
        // when & then
        assertThatThrownBy(() -> Name.from(null))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("이름은 필수입니다.");
    }
}
