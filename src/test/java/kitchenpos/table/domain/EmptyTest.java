package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("빈 테이블 테스트")
class EmptyTest {

    @DisplayName("isEmpty 테스트")
    @Test
    void isEmpty() {
        // given
        boolean value = true;

        // when
        Empty empty = Empty.of(value);

        // then
        assertAll(
                () -> assertThat(empty).isNotNull()
                , () -> assertThat(empty.isEmpty()).isTrue()
        );
    }

    @DisplayName("isNotEmpty 테스트")
    @Test
    void isNotEmpty() {
        // given
        boolean value = false;

        // when
        Empty empty = Empty.of(value);

        // then
        assertAll(
                () -> assertThat(empty).isNotNull()
                , () -> assertThat(empty.isNotEmpty()).isTrue()
        );
    }
}
