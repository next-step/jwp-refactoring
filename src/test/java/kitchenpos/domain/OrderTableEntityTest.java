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

    @DisplayName("비움 여부를 변경한다.")
    @Test
    void changeEmpty() {
        OrderTableEntity orderTable = new OrderTableEntity(0, false);

        orderTable.changeEmpty(true);

        assertThat(orderTable.isEmpty()).isTrue();
    }

    @DisplayName("단체 지정된 테이블의 비움 여부를 변경한다.")
    @Test
    void changeEmptyWithGrouped() {
        OrderTableEntity orderTable = new OrderTableEntity(0, false);
        orderTable.bindTo(new TableGroupEntity());

        assertThatThrownBy(() -> {
            orderTable.changeEmpty(true);
        }).isInstanceOf(CannotChangeEmptyException.class)
        .hasMessageContaining("비움 여부를 변경할 수 없습니다.");
    }

    @DisplayName("테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuest() {
        OrderTableEntity orderTable = new OrderTableEntity(0, false);

        orderTable.changeNumberOfGuests(5);

        assertThat(orderTable.getNumberOfGuests().getValue()).isEqualTo(5);
    }

    @DisplayName("테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuestWithEmpty() {
        OrderTableEntity orderTable = new OrderTableEntity(0, true);

        assertThatThrownBy(() -> {
            orderTable.changeNumberOfGuests(5);
        }).isInstanceOf(CannotChangeNumberOfGuestsException.class)
        .hasMessageContaining("손님 수를 변경할 수 없습니다.");
    }
}
