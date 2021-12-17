package kitchenpos.table.domain.table;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("주문 테이블 관리")
class OrderTableTest {

    @Nested
    @DisplayName("주문 테이블 생성")
    class CreateOrderTable {
        @Test
        @DisplayName("성공")
        public void create() {
            // given
            final int guestNumber = 1;
            final boolean orderTableEmpty = true;

            // when
            final OrderTable orderTable = new OrderTable(guestNumber, orderTableEmpty);

            // then
            assertAll(
                    () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(guestNumber),
                    () -> assertThat(orderTable.isEmpty()).isEqualTo(orderTableEmpty)
            );
        }

        @Test
        @DisplayName("실패 - 잘못된 방문자 수")
        public void failGuestNumber() {
            // given
            final int guestNumber = -1;
            final boolean orderTableEmpty = true;

            // when
            assertThatThrownBy(() -> {
                OrderTable orderTable = new OrderTable(guestNumber, orderTableEmpty);
            }).isInstanceOf(IllegalArgumentException.class);
        }
    }

}
