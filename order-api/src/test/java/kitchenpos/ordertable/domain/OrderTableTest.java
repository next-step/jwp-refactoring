package kitchenpos.ordertable.domain;

import kitchenpos.tablegroup.fixture.TableGroupTestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import kitchenpos.tablegroup.domain.TableGroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderTableTest {

    @DisplayName("주문테이블 생성 작업을 성공한다.")
    @Test
    void of() {
        // given
        int expectedNumberOfGuests = 10;
        boolean expectedIsEmpty = false;

        // when
        OrderTable orderTable =
                OrderTable.of(expectedNumberOfGuests, expectedIsEmpty);

        // then
        assertAll(
                () -> assertThat(orderTable).isNotNull(),
                () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(expectedNumberOfGuests),
                () -> assertThat(orderTable.isEmpty()).isEqualTo(expectedIsEmpty)
        );
    }

    @DisplayName("고객 수 변경 작업을 성공한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        int expectedNumberOfGuests = 20;
        OrderTable orderTable = OrderTable.of(10, false);

        // when
        orderTable.changeNumberOfGuests(expectedNumberOfGuests);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(expectedNumberOfGuests);
    }

    @DisplayName("고객 수 변경할 때, 고객의 수가 0보다 작으면 IllegalArgumentException을 반환한다.")
    @Test
    void changeNumberOfGuestsWithException1() {
        // given
        OrderTable orderTable = OrderTable.of(10, false);

        // when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("고객 수 변경할 때, 빈좌석이면 IllegalArgumentException을 반환한다.")
    @Test
    void changeNumberOfGuestsWithException2() {
        // given
        OrderTable orderTable = OrderTable.of(10, true);

        // when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(20))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈좌석 상태 변경 작업을 성공한다.")
    @Test
    void changeEmpty() {
        // given
        boolean expectedIsEmpty = false;
        OrderTable orderTable = OrderTable.of(10, true);

        // when
        orderTable.changeEmpty(expectedIsEmpty);

        // then
        assertThat(orderTable.isEmpty()).isEqualTo(expectedIsEmpty);
    }

    @DisplayName("빈좌석 상태 변경 작업할 때, 그룹 지정이 되어 있으면 IllegalArgumentException을 반환한다.")
    @Test
    void changeEmptyWithException1() {
        // given
        OrderTable orderTable = OrderTable.of(10, true);
        orderTable.group(1L);

        // when & then
        assertThatThrownBy(() -> orderTable.changeEmpty(false))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블그룹 변경 작업을 성공한다.")
    @Test
    void changeTableGroup() {
        // given
        TableGroup expectedTableGroup = TableGroupTestFixture.테이블그룹();
        OrderTable orderTable = OrderTable.of(10, true);

        // when
        orderTable.group(expectedTableGroup.getId());

        // then
        assertThat(orderTable.getTableGroupId()).isEqualTo(expectedTableGroup.getId());
    }
}
