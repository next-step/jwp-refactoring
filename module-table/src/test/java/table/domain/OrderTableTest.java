package table.domain;

import table.domain.OrderTable;
import table.domain.TableGroup;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderTableTest {

    @Test
    void 테이블_생성() {
        OrderTable orderTable = new OrderTable(0, true);

        assertAll(
                () -> assertThat(orderTable.getNumberOfGuests()).isNotNull(),
                () -> assertThat(orderTable.isEmpty()).isTrue()
        );
    }

    @Test
    void 테이블_상태_변경() {
        OrderTable orderTable = new OrderTable(0, true);

        orderTable.changeEmpty(false);

        assertThat(orderTable.isEmpty()).isFalse();
    }

    @Test
    void 테이블_인원수_변경() {
        OrderTable orderTable = new OrderTable(0, false);

        orderTable.changeNumberOfGuests(3);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(3);
    }

    @Test
    void 변경할_테이블_인원수가_음수인경우() {
        OrderTable orderTable = new OrderTable(0, false);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 비어있는_테이블의_인원을_변경하는_경우() {
        OrderTable orderTable = new OrderTable(0, true);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(5))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정된_테이블의_상태를_변경하는_경우() {
        OrderTable orderTable = new OrderTable(new TableGroup(1L),0, true);

        assertThatThrownBy(() -> orderTable.changeEmpty(false))
                .isInstanceOf(IllegalArgumentException.class);
    }
}