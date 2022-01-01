package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class EmptyTest {

    @DisplayName("주문 테이블 빈 여부를 생성할 수 있다.")
    @ParameterizedTest(name = "{displayName} [{index}] {argumentsWithNames}")
    @ValueSource(booleans = {true, false})
    void create(final boolean empty) {
        // when
        final ThrowableAssert.ThrowingCallable request = () -> new Empty(empty);

        // then
        assertThatNoException().isThrownBy(request);
    }

    @DisplayName("주문 테이블이 비어있음을 확인할 수 있다.")
    @Test
    void isEmpty() {
        // given
        final Empty empty = new Empty(true);

        // when
        final boolean isEmpty = empty.isEmpty();

        // then
        assertThat(isEmpty).isTrue();
    }

    @DisplayName("주문 테이블이 차지됨을 확인할 수 있다.")
    @Test
    void isTaken() {
        // given
        final Empty empty = new Empty(false);

        // when
        final boolean isTaken = empty.isTaken();

        // then
        assertThat(isTaken).isTrue();
    }

    @DisplayName("주문 테이블 빈 여부 간 동등성을 비교할 수 있다.")
    @Test
    void equals() {
        // given
        final boolean empty = true;

        // when
        final Empty empty1 = new Empty(empty);
        final Empty empty2 = new Empty(empty);

        // then
        assertThat(empty1).isEqualTo(empty2);
    }
}
