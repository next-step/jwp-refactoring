package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import static kitchenpos.fixture.TableGroupTestFixture.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderTableTest {

    @Test
    void of() {
        // given
        int expectedNumberOfGuests = 10;
        boolean expectedIsEmpty = false;

        // when
        OrderTable orderTable =
                OrderTable.of(createTableGroup(), expectedNumberOfGuests, expectedIsEmpty);

        // then
        assertAll(
                () -> assertThat(orderTable).isNotNull(),
                () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(expectedNumberOfGuests),
                () -> assertThat(orderTable.isEmpty()).isEqualTo(expectedIsEmpty)
        );
    }
}
