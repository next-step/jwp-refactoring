package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {

    @Test
    @DisplayName("손님 수는 0 보다 커야한다.")
    void changeNumberOfGuests_overZero() {
        OrderTable table = new OrderTable();

        assertThatThrownBy(() -> table.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("손님 수는 0보다 큰 값을 입력해 주세요.");
    }

    @Test
    @DisplayName("빈 테이블에는 게스트를 입력불가")
    void changeNumberOfGuests_emtyTable() {
        OrderTable table = new OrderTable();

        assertThatThrownBy(() -> table.changeNumberOfGuests(3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블에는 게스트를 입력할 수 없습니다.");
    }

    @Test
    @DisplayName("게스트를 입력됨")
    void changeNumberOfGuests() {
        OrderTable table = new OrderTable(2, false);

        table.changeNumberOfGuests(3);

        assertThat(table.getNumberOfGuests()).isEqualTo(3);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("단체 지정된 테이블은 빈 테이블 설정/해지 불가")
    void changEmpty_grouptTable(boolean emptyStatus) {
        OrderTable table = new OrderTable(1L, 3, emptyStatus);

        assertThatThrownBy(() -> table.changeEmpty(true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 지정된 테이블은 빈 테이블 설정/해지할 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("빈 테이블 설정/해지")
    void changeNumberOfGuests(boolean emptyStatus) {
        OrderTable table = new OrderTable(null, 3, false);

        table.changeEmpty(emptyStatus);

        assertThat(table.isEmpty()).isEqualTo(emptyStatus);
    }
}