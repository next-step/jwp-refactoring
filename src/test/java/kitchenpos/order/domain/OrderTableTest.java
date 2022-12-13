package kitchenpos.order.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.stream;

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
        orderTable.setEmpty(true);

        boolean canBeAdded = orderTable.canBeAddedToTableGroup();

        assertThat(canBeAdded).isTrue();
    }

    @Test
    @DisplayName("주문 테이블이 비어있지 않으면 단체 지정으로 추가가 불가")
    void notEmptyOrderTableCanNotBeAddedToTableGroup() {
        orderTable.setEmpty(false);
        boolean canBeAdded = orderTable.canBeAddedToTableGroup();

        assertThat(canBeAdded).isFalse();
    }

    @Test
    @DisplayName("주문 테이블이 이미 단체 지정이 되어 있으면 추가가 불가")
    void alreadyAddedToTableGroupOrderTableCanNotBeAddedToTableGroup() {
        ReflectionTestUtils.setField(orderTable,"tableGroup",TableGroup.of(1L, LocalDateTime.now()));

        boolean canBeAdded = orderTable.canBeAddedToTableGroup();

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
        orderTable.setEmpty(true);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(newNumberOfGuests))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블에 방문한 손님 수는 음수 일 수 없음")
    void setEmptyTrue() {
        orderTable.setEmpty(true);

        assertThat(orderTable.isEmpty()).isTrue();
    }
    @Test
    @DisplayName("주문 테이블에 방문한 손님 수는 음수 일 수 없음")
    void setEmptyFalse() {
        orderTable.setEmpty(false);

        assertThat(orderTable.isEmpty()).isFalse();
    }
}