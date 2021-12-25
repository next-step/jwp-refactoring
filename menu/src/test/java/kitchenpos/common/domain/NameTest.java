package kitchenpos.common.domain;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("이름 테스트")
class NameTest {
    @DisplayName("이름 생성 확인")
    @Test
    void 이름_생성_확인() {
        // given
        String 이름_요청_데이터 = "이름";

        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> Name.of(이름_요청_데이터);

        // then
        assertThatNoException().isThrownBy(생성_요청);
    }

    @DisplayName("이름은 Null일 수 없음")
    @Test
    void 이름은_Null일_수_없음() {
        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> Name.of(null);

        // then
        assertThatThrownBy(생성_요청).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이름은 비어 있을 수 없음")
    @Test
    void 이름은_비어_있을_수_없음() {
        // given
        String 이름_요청_데이터 = "";

        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> Name.of(이름_요청_데이터);

        // then
        assertThatThrownBy(생성_요청).isInstanceOf(IllegalArgumentException.class);
    }
}
