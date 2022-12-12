package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import static kitchenpos.fixture.TableGroupTestFixture.createTableGroup;
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
                OrderTable.of(expectedId, createTableGroup(), expectedNumberOfGuests, expectedIsEmpty);

        // then
        assertAll(
                () -> assertThat(orderTable).isNotNull(),
                () -> assertThat(orderTable.getId()).isEqualTo(expectedId),
                () -> assertThat(orderTable.getTableGroup().getId()).isEqualTo(expectedTableGroupId),
                () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(expectedNumberOfGuests),
                () -> assertThat(orderTable.isEmpty()).isEqualTo(expectedIsEmpty)
        );
    }
}
