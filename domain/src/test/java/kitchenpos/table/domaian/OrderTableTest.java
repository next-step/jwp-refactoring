package kitchenpos.table.domaian;

import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.util.TestFixture.*;
import static kitchenpos.util.TestFixture.빈_주문테이블_1_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("OrderTable Domain Test")
class OrderTableTest {
    @Test
    void changeGroupTest() {
        // given
        OrderTable orderTable = 주문테이블_1_생성();

        // when
        orderTable.changeGroup(주문테이블_1_생성().getTableGroupId());

        // then
        assertThat(orderTable.isEmpty()).isFalse();
    }

    @Test
    void changeEmptyTest() {
        // given
        OrderTable orderTable = 주문테이블_1_생성();

        // when
        orderTable.changeEmpty();

        // then
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @Test
    void changeNumberOfGuestsTest() {
        // given
        OrderTable orderTable = 주문테이블_1_생성();

        // when
        orderTable.changeNumberOfGuests(1000);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(1000);
    }

    @Test
    void changeNumberOfGuestsExceptionTest() {
        // given
        OrderTable orderTable = 주문테이블_1_생성();

        // then
        assertThatThrownBy(() -> {
            orderTable.changeNumberOfGuests(-1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void validEmptyTest() {
        // given
        OrderTable orderTable = 빈_주문테이블_1_생성();

        // then
        assertThatThrownBy(() -> {
            orderTable.validEmpty();
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeUnGroupTest() {
        // given
        OrderTable orderTable = 빈_주문테이블_1_생성();

        // when
        orderTable.changeUnGroup();

        // then
        assertThat(orderTable.getTableGroupId()).isNull();
    }
}
