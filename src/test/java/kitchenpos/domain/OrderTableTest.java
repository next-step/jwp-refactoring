package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import static kitchenpos.fixture.TableGroupTestFixture.테이블그룹;
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
                OrderTable.of(테이블그룹(), expectedNumberOfGuests, expectedIsEmpty);

        // then
        assertAll(
                () -> assertThat(orderTable).isNotNull(),
                () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(expectedNumberOfGuests),
                () -> assertThat(orderTable.isEmpty()).isEqualTo(expectedIsEmpty)
        );
    }

    @Test
    void changeNumberOfGuests() {
        // given
        int expectedNumberOfGuests = 20;
        OrderTable orderTable = OrderTable.of(테이블그룹(), 10, false);

        // when
        orderTable.changeNumberOfGuests(expectedNumberOfGuests);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(expectedNumberOfGuests);
    }

    @Test
    void changeEmpty() {
        // given
        boolean expectedIsEmpty = false;
        OrderTable orderTable = OrderTable.of(null, 10, true);

        // when
        orderTable.changeEmpty(expectedIsEmpty);

        // then
        assertThat(orderTable.isEmpty()).isEqualTo(expectedIsEmpty);
    }

    @Test
    void changeTableGroup() {
        // given
        TableGroup expectedTableGroup = 테이블그룹();
        OrderTable orderTable = OrderTable.of(null, 10, true);

        // when
        orderTable.changeTableGroup(expectedTableGroup);

        // then
        assertThat(orderTable.getTableGroup()).isEqualTo(expectedTableGroup);
    }
}
