package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("고객수 포장 객체 테스트")
class NumberOfGuestsTest {

    @DisplayName("생성")
    @Test
    void create() {
        // given
        NumberOfGuests numberOfGuests = new NumberOfGuests(10);
        // when
        // then
        assertThat(numberOfGuests).isNotNull();
    }
}