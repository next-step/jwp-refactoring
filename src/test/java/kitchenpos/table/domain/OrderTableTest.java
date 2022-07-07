package kitchenpos.table.domain;

import kitchenpos.table.exception.TableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTableTest {
    @Test
    @DisplayName("주문 테이블 생성")
    void create() {
        // when
        final OrderTable orderTable = OrderTable.of(0, true);
        // then
        assertThat(orderTable).isInstanceOf(OrderTable.class);
    }

    @Test
    @DisplayName("테이블 빈 상태 변경")
    void empty() {
        // given
        final OrderTable table = OrderTable.of(0, false);
        final OrderTable updateTable = OrderTable.of(0,true);
        // when
        table.updateEmptyTable(updateTable);
        // then
        assertThat(table.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("테이블 빈 상태 변경 그룹 테이블 오류")
    void emptyException() {
        // given
        final OrderTable table = OrderTable.of(0, false);
        table.updateTableGroupId(1L);
        final OrderTable updateTable = OrderTable.of(0,true);
        // when
        assertThatThrownBy(() -> table.updateEmptyTable(updateTable))
                .isInstanceOf(TableException.class);
    }

    @Test
    @DisplayName("손님 수 변경")
    void changeNumberOfGuests() {
        // given
        final OrderTable table = OrderTable.of(3, false);
        final OrderTable updateTable = OrderTable.of(5,false);
        // when
        table.changeNumberOfGuest(updateTable);
        // then
        assertThat(table.getNumberOfGuests()).isEqualTo(5);
    }

    @Test
    @DisplayName("손님 수 변경 시 손님 수가 0보다 작은 값 일떄 오류")
    void changeNumberOfGuestsException() {
        // given
        final OrderTable table = OrderTable.of(-1, false);
        final OrderTable updateTable = OrderTable.of(0,true);
        // when
        assertThatThrownBy(() -> table.changeNumberOfGuest(updateTable))
                .isInstanceOf(TableException.class);
    }

    @Test
    @DisplayName("손님 수 변경 시 테이블이 비어 있지 않을때 오류 ")
    void changeNumberOfGuestsException2() {
        // given
        final OrderTable table = OrderTable.of(3, true);
        final OrderTable updateTable = OrderTable.of(5,true);
        // when
        assertThatThrownBy(() -> table.changeNumberOfGuest(updateTable))
                .isInstanceOf(TableException.class);
    }
}
