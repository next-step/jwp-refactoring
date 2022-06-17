package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("OrderTable 클래스 테스트")
class OrderTableEntityTest {

    @DisplayName("빈 OrderTable을 생성한다.")
    @Test
    void createEmptyTable() {
        OrderTableEntity orderTable = new OrderTableEntity(0, true);
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @DisplayName("비어있지 않은 OrderTable을 생성한다.")
    @Test
    void createNotEmptyTable() {
        OrderTableEntity orderTable = new OrderTableEntity(5, false);
        assertThat(orderTable.isEmpty()).isFalse();
    }

    @DisplayName("손님의 수가 -1인 OrderTable을 생성한다.")
    @Test
    void failureCreateWithNegativeNumberOfGuests() {
        assertThatThrownBy(() -> {
            new OrderTableEntity(-1, false);
        }).isInstanceOf(InvalidNumberOfGuestsException.class)
        .hasMessageContaining("유효하지 않은 손님 수입니다.");
    }
}