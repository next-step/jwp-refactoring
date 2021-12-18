package kitchenpos.table.domain;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@DisplayName("테이블 상태 테스트")
class EmptyTest {
    @DisplayName("테이블 상태 생성 확인")
    @Test
    void 테이블_상태_생성_확인() {
        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> Empty.of(true);

        // then
        assertThatNoException().isThrownBy(생성_요청);
    }

    @DisplayName("빈 테이블 확인")
    @Test
    void 빈_테이블_확인() {
        // given
        Empty 테이블_상태 = Empty.of(true);

        // when
        boolean 빈_테이블_확인 = 테이블_상태.isEmpty();

        // then
        assertThat(빈_테이블_확인).isTrue();
    }
}
