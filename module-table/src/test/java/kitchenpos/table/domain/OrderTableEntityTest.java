package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class OrderTableEntityTest {

  @DisplayName("손님수, 주문을 등록할 수 있는 테이블 여부를 입력받아 테이블을 만든다.")
  @Test
  void createTest() {
    //given
    int numberOfGuest = 4;

    //when
    OrderTableEntity orderTableEntity = new OrderTableEntity(numberOfGuest, true);

    //then
    assertAll(
        () -> assertThat(orderTableEntity.getTableGroupId()).isNull(),
        () -> assertThat(orderTableEntity.getNumberOfGuests()).isEqualTo(numberOfGuest),
        () -> assertThat(orderTableEntity.isEmpty()).isTrue()
    );
  }

  @DisplayName("주문을 등록할 수 있는 테이블 여부를 변경할 수 있다.")
  @Test
  void changeEmptyTest() {
    //given
    int numberOfGuest = 4;
    OrderTableEntity orderTableEntity = new OrderTableEntity(numberOfGuest, true);

    //when
    orderTableEntity.changeEmpty(false);

    //then
    assertAll(
        () -> assertThat(orderTableEntity.getTableGroupId()).isNull(),
        () -> assertThat(orderTableEntity.getNumberOfGuests()).isEqualTo(numberOfGuest),
        () -> assertThat(orderTableEntity.isEmpty()).isFalse()
    );
  }

  @DisplayName("테이블 그룹에 속하지 않을 때만 주문을 등록할 수 있는 테이블 여부를 변경할 수 있다.")
  @Test
  void changeEmptyFailCauseTableGroupTest() {
    //given
    int numberOfGuest = 4;
    OrderTableEntity orderTableEntity = OrderTableEntity.initWithTableGroupId(1L, numberOfGuest, true);

    //when & then
    assertThatThrownBy(() -> orderTableEntity.changeEmpty(false)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("테이블의 손님수를 변경할 수 있다.")
  @Test
  void changeNumberOfGuestsTest() {
    //given
    int numberOfGuest = 4;
    int toChangeNumber = 5;
    OrderTableEntity orderTableEntity = new OrderTableEntity(numberOfGuest, false);

    //when
    orderTableEntity.changeNumberOfGuests(toChangeNumber);

    //then
    assertAll(
        () -> assertThat(orderTableEntity.getTableGroupId()).isNull(),
        () -> assertThat(orderTableEntity.getNumberOfGuests()).isEqualTo(toChangeNumber),
        () -> assertThat(orderTableEntity.isEmpty()).isFalse()
    );
  }

  @DisplayName("테이블의 손님수는 0 이상이어야 한다.")
  @Test
  void changeNumberOfGuestsFailCauseNegativeNumberTest() {
    //given
    int numberOfGuest = 4;
    int toChangeNumber = -1;
    OrderTableEntity orderTableEntity = new OrderTableEntity(numberOfGuest, false);

    //when & then
    assertThatThrownBy(() -> orderTableEntity.changeNumberOfGuests(toChangeNumber)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("주문을 등록할 수 있는 테이블만 손님수를 변경할 수 있다.")
  @Test
  void changeNumberOfGuestsFailCauseEmptyTableTest() {
    //given
    int numberOfGuest = 4;
    int toChangeNumber = 5;
    OrderTableEntity orderTableEntity = new OrderTableEntity(numberOfGuest, true);

    //when & then
    assertThatThrownBy(() -> orderTableEntity.changeNumberOfGuests(toChangeNumber)).isInstanceOf(IllegalArgumentException.class);
  }

}
