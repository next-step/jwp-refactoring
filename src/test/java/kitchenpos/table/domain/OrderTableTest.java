package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 테이블 테스트")
class OrderTableTest {

    @DisplayName("주문 테이블 생성 성공 테스트")
    @Test
    void instantiate_success() {
        // given
        NumberOfGuests numberOfGuests = NumberOfGuests.of(4);
        Empty empty = Empty.of(false);

        // when
        OrderTable orderTable = OrderTable.of(numberOfGuests, empty);

        // then
        assertAll(
                () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests)
                , () -> assertThat(orderTable.getEmpty()).isEqualTo(empty)
        );
    }
}
