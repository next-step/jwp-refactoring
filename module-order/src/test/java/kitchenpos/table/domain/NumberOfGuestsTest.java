package kitchenpos.table.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("손님 수 테스트")
class NumberOfGuestsTest {

    @DisplayName("손님 수 생성 성공 테스트")
    @ParameterizedTest(name = "{displayName}{index} -> guests: {0}")
    @ValueSource(ints = {0, 1})
    void instantiate_success(int guests) {
        // when
        NumberOfGuests numberOfGuests = NumberOfGuests.of(guests);

        // then
        assertAll(
                () -> assertThat(numberOfGuests).isNotNull()
                , () -> assertThat(numberOfGuests.getNumberOfGuests()).isEqualTo(guests)
        );
    }

    @DisplayName("손님 수 생성 실패 테스트")
    @Test
    void instantiate_failure() {
        // given
        int guests = -1;

        // when & then
        Assertions.assertThatIllegalArgumentException()
                .isThrownBy(() -> NumberOfGuests.of(guests));
    }
}
