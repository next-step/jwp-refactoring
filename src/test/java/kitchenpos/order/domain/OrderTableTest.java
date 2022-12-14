package kitchenpos.order.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class OrderTableTest {

    private OrderTable orderTable;
    private int initialNumberOfGuest = 4;

    @BeforeEach
    void setUp() {
        orderTable = OrderTable.of(1L, null, initialNumberOfGuest, false);
    }

    @Test
    @DisplayName("주문 테이블이 비어있고, 단제 지정이 되어있지 않으면 단체 지정으로 추가가 가능함")
    void canBeAddedToTableGroup() {
        orderTable.updateEmpty(true, false);

        boolean canBeAdded = orderTable.canBeAddedToTableGroup();

        assertThat(canBeAdded).isTrue();
    }

    @Test
    @DisplayName("주문 테이블이 비어있지 않으면 단체 지정으로 추가가 불가")
    void notEmptyOrderTableCanNotBeAddedToTableGroup() {
        orderTable.updateEmpty(false, false);
        boolean canBeAdded = orderTable.canBeAddedToTableGroup();

        assertThat(canBeAdded).isFalse();
    }

    @Test
    @DisplayName("주문 테이블이 이미 단체 지정이 되어 있으면 추가가 불가")
    void alreadyAddedToTableGroupOrderTableCanNotBeAddedToTableGroup() {
        orderTable.updateEmpty(true, false);
        OrderTable spy = spy(orderTable);
        given(spy.getTableGroup()).willReturn(new TableGroup());

        boolean canBeAdded = spy.canBeAddedToTableGroup();

        assertThat(canBeAdded).isFalse();
    }

    @Test
    @DisplayName("주문 테이블에 방문한 손님 수를 변경함")
    void changeNumberOfGuests() {
        orderTable.changeNumberOfGuests(10);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(10);
    }

    @Test
    @DisplayName("주문 테이블에 방문한 손님 수는 음수 일 수 없음")
    void throwIfNumberOfGuestsIsNegative() {
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 주문 테이블은 손님 수를 변경할 수 없음")
    void canNotChangeNumberOfGuestOfEmptyTable() {
        int newNumberOfGuests = 10;
        orderTable.updateEmpty(true, false);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(newNumberOfGuests))
                .isInstanceOf(IllegalArgumentException.class);
    }
}