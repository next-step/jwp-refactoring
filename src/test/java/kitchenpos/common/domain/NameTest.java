package kitchenpos.common.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.common.Messages.NAME_CANNOT_EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

class NameTest {

    @Test
    @DisplayName("이름 생성 테스트")
    void name() {
        // given
        Name name = Name.of("치킨");

        // when
        // then
        assertAll(
                () -> assertThat(name).isNotNull(),
                () -> assertThat(name.getName()).isEqualTo("치킨")
        );
    }

    @Test
    @DisplayName("이름이 비어있는 경우 생성 실패")
    void nameCannotEmpty() {
        // given
        // when
        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Name.of(""))
                .withMessage(NAME_CANNOT_EMPTY)
        ;
    }
}
