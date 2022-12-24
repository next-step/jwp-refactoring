package kitchenpos.table;

import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTableTest {

    @DisplayName("주문테이블 빈상태로 변경 테스트")
    @Test
    void orderTableChangeEmptyTest() {
        //given
        final OrderTable orderTable = new OrderTable(1L, null, new NumberOfGuests(10), false);
        //when
        orderTable.changeEmpty(true);
        //then
        assertThat(orderTable.isEmpty())
                .isTrue();
    }

    @DisplayName("주문테이블 비지 않은 상태로 변경 테스트")
    @Test
    void orderTableChangeNotEmptyTest() {
        //given
        final OrderTable orderTable = new OrderTable(1L, null, new NumberOfGuests(10), true);
        //when
        orderTable.changeEmpty(false);
        //then
        assertThat(orderTable.isEmpty())
                .isFalse();
    }

    @DisplayName("그룹화 되어있는 테이블 변경 오류 테스트")
    @Test
    void groupingOrderTableChangeExceptionTest() {
        //given
        final TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now());
        final OrderTable orderTable1 = new OrderTable(1L, tableGroup, new NumberOfGuests(10), false);

        //when
        //then
        assertThatThrownBy(() -> orderTable1.changeEmpty(true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 손님 숫자 변경 테스트")
    @Test
    void changeNumberOfGuestsTest() {
        //given
        final TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now());
        final OrderTable orderTable1 = new OrderTable(1L, tableGroup, new NumberOfGuests(10), false);
        int newNumberOfGuests = 2;

        //when
        orderTable1.changeNumberOfGuests(new NumberOfGuests(newNumberOfGuests));

        //then
        assertThat(orderTable1.getNumberOfGuests())
                .isEqualTo(new NumberOfGuests(newNumberOfGuests));
    }

    @DisplayName("빈 테이블 손님 숫자 변경 불가 테스트")
    @Test
    void emptyChangeNumberOfGuestsExceptionTest() {
        //given
        final OrderTable orderTable1 = new OrderTable(1L, null, new NumberOfGuests(10), true);

        //when
        assertThatThrownBy(() -> orderTable1.changeNumberOfGuests(new NumberOfGuests(5)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 그루핑 되어있는 테이블 유효성 체크")
    @Test
    void notEmptyValidateTableGroupTest() {
        //given
        final TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now());
        final OrderTable orderTable1 = new OrderTable(1L, tableGroup, new NumberOfGuests(10), true);

        //when
        //then
        assertThatThrownBy(() -> orderTable1.validateTableGroup())
                .isInstanceOf(IllegalArgumentException.class);
    }

}
