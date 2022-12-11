package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderTableTest {

    @Test
    void of() {
        // given
        long expectedId = 1L;
        Long expectedTableGroupId = 1L;
        int expectedNumberOfGuests = 10;
        boolean expectedIsEmpty = false;

        // when
        OrderTable orderTable =
                OrderTable.of(expectedId, expectedTableGroupId, expectedNumberOfGuests, expectedIsEmpty);

        // then
        assertAll(
                () -> assertThat(orderTable).isNotNull(),
                () -> assertThat(orderTable.getId()).isEqualTo(expectedId),
                () -> assertThat(orderTable.getTableGroupId()).isEqualTo(expectedTableGroupId),
                () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(expectedNumberOfGuests),
                () -> assertThat(orderTable.isEmpty()).isEqualTo(expectedIsEmpty)
        );
    }
}
