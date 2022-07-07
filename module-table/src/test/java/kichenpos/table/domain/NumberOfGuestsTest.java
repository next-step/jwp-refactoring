package kichenpos.table.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NumberOfGuestsTest {

    @Test
    void 방문한_손님은_최소_0명_이상이다() {
        // when & then
        assertThatThrownBy(() ->
                new NumberOfGuests(-1)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("방문한 손님의 수가 0보다 작으면 손님의 수를 변경할 수 없습니다.");
    }
}
