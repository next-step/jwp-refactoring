package kitchenpos.ordertable.domain;

import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("주문 테이블")
class OrderTableTest {

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void create() {
        // given
        int numberOfGuests = 5;
        boolean empty = true;

        // when
        OrderTable orderTable = new OrderTable(numberOfGuests, empty);

        // then
        assertThat(orderTable).isNotNull();
    }

    @DisplayName("주문 테이블의 등록 가능 상태를 변경한다.")
    @Test
    void changeEmpty() {
        // given
        OrderTable orderTable = new OrderTable(5, true);

        // when
        orderTable.changeEmpty(false);

        // then
        assertThat(orderTable.isEmpty()).isFalse();
    }

    // todo 주문 도메인 개발 후 진행 예정
    @DisplayName("주문 테이블의 주문이 조리 중이거나 식사 중일때는 상태를 변경할 수 없다.")
    @Test
    void cantChangeEmpty1() {
        // given


        // when


        // then

    }

    @DisplayName("단체 지정이 되어 있다면 상태를 변경할 수 없다.")
    @Test
    void cantChangeEmpty2() {
        // given
        OrderTable orderTable = new OrderTable(new TableGroup(new ArrayList<>()), 5, true);

        // when / then
        assertThrows(IllegalStateException.class, () -> orderTable.changeEmpty(false));
    }

    @DisplayName("주문 테이블에 손님 수를 변한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTable orderTable = new OrderTable( 5, false);

        // when
        orderTable.changeNumberOfGuests(3);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(3);
    }

    @DisplayName("손님 수 변경 시 0명 이상이어야 한다.")
    @Test
    void requiredNumberOfGuests() {
        // given
        OrderTable orderTable = new OrderTable( 5, false);

        // when / then
        assertThrows(IllegalArgumentException.class, () -> orderTable.changeNumberOfGuests(-1));

    }

    @DisplayName("등록 불가 상태인 주문 테이블인 경우 손님 수를 등록할 수 없다.")
    @Test
    void cantChangeNumberOfGuests() {
        // given
        OrderTable orderTable = new OrderTable( 5, true);

        // when / then
        assertThrows(IllegalStateException.class, () -> orderTable.changeNumberOfGuests(-1));
    }

}
