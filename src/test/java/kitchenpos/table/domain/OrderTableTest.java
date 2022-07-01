package kitchenpos.table.domain;

import kitchenpos.embeddableEntity.Empty;
import kitchenpos.table.dto.OrderTableRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class OrderTableTest {
    NumberOfGuests numberOfGuests;
    Empty empty;
    OrderTableRequest orderTableRequest;

    @BeforeEach
    void setUp() {
        numberOfGuests = new NumberOfGuests(10);
        empty = new Empty(false);
        orderTableRequest = new OrderTableRequest(numberOfGuests.value(), empty.value());
    }

    @Test
    @DisplayName("OrderTableRequest 로 OrderTable 인스턴스를 생성한다")
    void of() {
        NumberOfGuests numberOfGuests = new NumberOfGuests(10);
        Empty empty = new Empty(false);
        OrderTableRequest orderTableRequest = new OrderTableRequest(numberOfGuests.value(), empty.value());

        // when
        OrderTable orderTable = OrderTable.of(orderTableRequest);

        // then
        assertAll(
                () -> assertThat(orderTable.getTableGroup()).isNull(),
                () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests),
                () -> assertThat(orderTable.getEmpty()).isEqualTo(empty)
        );
    }

    @Test
    @DisplayName("그룹이 존재할 경우 예외가 발생한다")
    void validate() {
        // given
        OrderTable orderTable = OrderTable.of(orderTableRequest);

        // when
        orderTable.changeTableGroupIdAndEmpty(new TableGroup());

        // then
        assertThatThrownBy(orderTable::validateGroupTable)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블이 비었으면 예외가 발생한다")
    void validateOrderTableEmptyTrue() {
        // given
        OrderTable orderTable = OrderTable.of(orderTableRequest);

        // when
        orderTable.changeEmpty(true);

        // then
        assertThatThrownBy(orderTable::validateOrderTableEmpty)
                .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    @DisplayName("주문 테이블의 손님 수를 10명에서 5명으로 변경한다")
    void changeNumberOfGuests() {
        // given
        OrderTable orderTable = OrderTable.of(orderTableRequest);
        NumberOfGuests numberOfGuests = new NumberOfGuests(5);

        // when
        orderTable.changeNumberOfGuests(numberOfGuests);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @Test
    @DisplayName("주문 테이블을 비운다")
    void changeEmpty() {
        // given
        OrderTable orderTable = OrderTable.of(orderTableRequest);

        // when
        orderTable.changeEmpty(true);

        // then
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("테이블이 비었으면 예외가 발생한다")
    void validateEmptyAndTableGroupEmptyException() {
        // given
        OrderTable orderTable = OrderTable.of(orderTableRequest);

        // when
        orderTable.changeEmpty(true);

        // then
        orderTable.validateEmptyAndTableGroup();
    }

    @Test
    @DisplayName("그룹 테이블이 존재하면 예외가 발생한다")
    void validateEmptyAndTableGroupGroupExistsException() {
        // given
        OrderTable orderTable = OrderTable.of(orderTableRequest);

        // when
        orderTable.changeTableGroupIdAndEmpty(new TableGroup());

        // then
        assertThatThrownBy(orderTable::validateEmptyAndTableGroup)
                .isInstanceOf(IllegalArgumentException.class);

    }
}
