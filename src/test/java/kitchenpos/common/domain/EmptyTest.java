package kitchenpos.common.domain;

import kitchenpos.core.Empty;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Empty 클래스 테스트")
class EmptyTest {

    @DisplayName("Empty를 true로 생성한다.")
    @Test
    void createWithTrue() {
        Empty empty = new Empty(true);
        assertThat(empty.isTrue()).isTrue();
    }

    @DisplayName("Empty를 false로 생성한다.")
    @Test
    void createWithFalse() {
        Empty empty = new Empty(false);
        assertThat(empty.isTrue()).isFalse();
    }
}
