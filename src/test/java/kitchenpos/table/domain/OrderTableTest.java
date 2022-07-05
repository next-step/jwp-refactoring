package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderTableTest {

    @Test
    @DisplayName("주문 테이블 등록시 정상적으로 손님수와 빈 테이블 등록 테스트")
    void createOrderTable() {

        // given
        NumberOfGuests numberOfGuests = NumberOfGuests.of(4);
        Empty empty = Empty.of(false);

        // when
        OrderTable orderTable = OrderTable.of(numberOfGuests, empty);

        // then
        assertAll(
                () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(4),
                () -> assertThat(orderTable.isEmpty()).isFalse()
        );
    }
}
