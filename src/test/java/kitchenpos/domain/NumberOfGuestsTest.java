package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.common.constant.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("방문한 손님 수 관련 도메인 테스트")
public class NumberOfGuestsTest {

    @DisplayName("방문한 손님 수를 생성한다.")
    @Test
    void createNumberOfGuests() {
        // given
        int actualNumberOfGuests = 10;

        // when
        NumberOfGuests numberOfGuests = NumberOfGuests.from(actualNumberOfGuests);

        // then
        assertAll(
                () -> assertThat(numberOfGuests.value()).isEqualTo(actualNumberOfGuests),
                () -> assertThat(numberOfGuests).isEqualTo(NumberOfGuests.from(actualNumberOfGuests))
        );
    }

    @ParameterizedTest(name = "방문한 손님 수가 0보다 작으면 에러가 발생한다. (방문한 손님 수: {0})")
    @ValueSource(ints = {-3, -2})
    void createNumberOfGuestsThrowErrorWhenNumberOfGuestsIsSmallerThanZero(int numberOfGuests) {
        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> NumberOfGuests.from(numberOfGuests))
                .withMessage(ErrorCode.방문한_손님_수는_0보다_작을_수_없음.getErrorMessage());
    }
}
