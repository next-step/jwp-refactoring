package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    @DisplayName("주문 테이블 객체가 같은지 검증")
    void verifyEqualsOrderTable() {
        final OrderTable orderTable = new OrderTable.Builder()
                .setId(1L)
                .setGuestNumber(GuestNumber.of(5))
                .setEmpty(false)
                .build();

        assertThat(orderTable).isEqualTo(new OrderTable.Builder()
                .setId(1L)
                .setGuestNumber(GuestNumber.of(5))
                .setEmpty(false)
                .build());
    }
}
