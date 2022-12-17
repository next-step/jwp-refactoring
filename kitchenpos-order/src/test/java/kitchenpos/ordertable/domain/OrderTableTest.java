package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {
    @DisplayName("손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable tableA = new OrderTable(0, false);

        tableA.changeNumberOfGuests(3);

        assertThat(tableA.getNumberOfGuests()).isEqualTo(3);
    }

    @DisplayName("테이블의 손님 수는 음수 일 수 없다.")
    @Test
    void createNumberOfGuestsIsNegative() {
        assertThatThrownBy(() -> new OrderTable(-1, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("손님 수는 음수 일 수 없습니다.");
    }

    @DisplayName("테이블 이용 여부를 변경할 수 있다.")
    @Test
    void changeEmpty() {
        OrderTable tableA = new OrderTable(0, false);

        tableA.changeEmpty(true);

        assertThat(tableA.isEmpty()).isTrue();
    }

    @DisplayName("테이블이 단체 그룹으로 지정되어 있는 경우 이용 여부를 변경할 수 없다.")
    @Test
    void changeEmptyIsGrouped() {
        OrderTable tableA = new OrderTable(5, false);
        tableA.groupBy(1L);

        assertThatThrownBy(() -> tableA.changeEmpty(true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 테이블로 지정되어 있습니다.");
    }

    @DisplayName("이용중이지 않은 테이블의 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsIsEmpty() {
        OrderTable tableA = new OrderTable(0, true);

        assertThatThrownBy(() -> tableA.changeNumberOfGuests(3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("사용중이지 않은 테이블은 손님 수를 변경할 수 없습니다.");
    }

    @DisplayName("단체 테이블로 지정할 수 있다.")
    @Test
    void isGrouped() {
        OrderTable tableA = new OrderTable(0, true);
        tableA.groupBy(1L);

        assertThat(tableA.isGrouped()).isTrue();
    }

    @DisplayName("이미 단체 테이블로 지정된 경우 단체 테이블로 지정할 수 없다.")
    @Test
    void groupBy() {
        OrderTable tableA = new OrderTable(0, true);
        tableA.groupBy(1L);

        assertThatThrownBy(() -> tableA.groupBy(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 테이블로 지정되어 있습니다.");
    }

    @DisplayName("단체 테이블 지정을 해제할 수 있다.")
    @Test
    void ungroup() {
        OrderTable tableA = new OrderTable(0, true);
        tableA.groupBy(1L);

        tableA.ungroup();

        assertThat(tableA.isGrouped()).isFalse();
    }
}
