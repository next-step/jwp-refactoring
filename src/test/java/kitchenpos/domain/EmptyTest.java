package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("비었는지 여부 관련 도메인 테스트")
public class EmptyTest {

    @DisplayName("비어있는 경우, 비어있는 객체를 반환한다.")
    @Test
    void isEmpty() {
        // given
        boolean isEmpty = true;

        // when
        Empty empty = Empty.valueOf(isEmpty);

        // then
        assertThat(empty.isEmpty()).isTrue();
    }

    @DisplayName("비어있지 않는 경우, 비어있지 않는 객체를 반환한다.")
    @Test
    void isNotEmpty() {
        // given
        boolean isEmpty = false;

        // when
        Empty empty = Empty.valueOf(isEmpty);

        // then
        assertThat(empty.isEmpty()).isFalse();
    }
}
