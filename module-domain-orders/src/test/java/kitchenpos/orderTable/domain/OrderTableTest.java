package kitchenpos.orderTable.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static kitchenpos.utils.TestUtils.getRandomId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 테이블")
class OrderTableTest {

    @ParameterizedTest
    @DisplayName("주문 테이블를 생성한다. 주문 테이블 그룹의 초기값은 존재하지 않는다.")
    @CsvSource({"0, true", "1, false"})
    public void initOrderTable(int numberOfGuests, boolean empty) {
        // when
        OrderTable orderTable = new OrderTable(getRandomId(), numberOfGuests);

        // then
        assertThat(orderTable).isNotNull();
        assertThat(orderTable.isEmpty()).isEqualTo(empty);
        assertThat(orderTable.getTableGroup()).isNull();
    }

    @Test
    @DisplayName("주문 테이블를 생성시, 손님수는 음수는 불가능하다.")
    public void exceptionInitOrderTable() {
        // then
        assertThatThrownBy(() -> new OrderTable(getRandomId(), -1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("손님 수는 음수는 불가능합니다.");
    }

    @ParameterizedTest
    @DisplayName("주문 테이블이 비어있으면 빈상태 여부를 변경할수 있다.")
    @ValueSource(booleans = {true, false})
    public void changeEmpty(boolean empty) {
        // given
        OrderTable orderTable = new OrderTable(getRandomId(), 0);

        // when
        orderTable.changeEmpty(empty);

        // then
        assertThat(orderTable.isEmpty()).isEqualTo(empty);
    }

    @ParameterizedTest
    @DisplayName("빈상태 수정시, 그룹 설정이 되어 있는 주문테이블은 상태를 변경할수 없습니다.")
    @ValueSource(booleans = {true, false})
    public void exceptionChangeEmpty(boolean empty) {
        // given
        OrderTable orderTable = new OrderTable(getRandomId(), 0);

        // when
        orderTable.setTableGroup(new TableGroup());

        // then
        assertThatThrownBy(() -> orderTable.changeEmpty(empty))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("그룹 설정이 되어 있는 주문테이블은 상태를 변경할수 없습니다.");
    }

    @DisplayName("비워있지 않은 주문 테이블의 손님수를 수정할수 있다.")
    @Test
    public void changeNumberOfGuests() {
        // given
        OrderTable orderTable = new OrderTable(getRandomId(), 1);

        // when
        orderTable.changeNumberOfGuests(10);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(10);
    }

    @DisplayName("주문 테이블이 비어있으면, 손님수를 수정할수 없다.")
    @Test
    public void exceptionChangeNumberOfGuests1() {
        // given
        OrderTable orderTable = new OrderTable(getRandomId(), 0);

        // then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당테이블은 비어있어 수정이 불가능합니다");
    }

    @DisplayName("손님수를 음수로 수정할수 없다.")
    @Test
    public void exceptionChangeNumberOfGuests2() {
        // given
        OrderTable orderTable = new OrderTable(getRandomId(), 1);

        // then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("손님 수는 음수는 불가능합니다.");
    }
}