package kitchenpos.ordertable.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("테이블 상태 도메인 테스트")
class EmptyTest {

    @ParameterizedTest(name = "{displayName} - [{index}] {argumentsWithNames}")
    @CsvSource(value = {"true:true", "false:false"}, delimiter = ':')
    @DisplayName("테이블 그룹 가능여부를 반환한다.")
    void isGroupable(boolean empty, boolean expected) {
        // when
        boolean groupable = new Empty(empty).isGroupable();

        // then
        assertThat(groupable).isEqualTo(expected);
    }

    @Test
    @DisplayName("방문한 손님 수를 변경 가능하지 않으면 예외를 발생한다.")
    void validateNumberOfGuestsChangable() {
        // given
        Empty empty = new Empty(true);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(empty::validateNumberOfGuestsChangable);
    }
}
