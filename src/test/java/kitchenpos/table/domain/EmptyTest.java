package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmptyTest {

    @Test
    @DisplayName("Empty 생성 테스트")
    void empty() {
        // given
        boolean value = true;

        // when
        Empty empty = Empty.of(value);

        // then
        assertThat(empty.isEmpty()).isTrue();
    }
}
