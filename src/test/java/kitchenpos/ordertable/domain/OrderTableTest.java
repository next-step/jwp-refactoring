package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.testfixtures.MenuTestFixtures;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.ordertable.exception.TableChangeNumberOfGuestsException;
import kitchenpos.ordertable.exception.TableUpdateStateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @DisplayName("주문 테이블 생성")
    @Test
    void constructor() {
        //given, when
        OrderTable orderTable = new OrderTable(6, false);
        OrderTable expectedTable = new OrderTable(6, false);

        //then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(expectedTable.getNumberOfGuests());
    }

    @DisplayName("테이블 상태 업데이트")
    @Test
    void updateEmpty() {
        //given
        OrderTable orderTable = new OrderTable(6, false);

        //when
        orderTable.updateTableStatus(true);

        //then
        assertThat(orderTable.isOrderClose()).isTrue();
    }

    @DisplayName("그룹화된 테이블은 상태 업데이트 불가")
    @Test
    void updateEmpty_exception1() {
        //given
        OrderTable orderTable1 = new OrderTable(1L, 6, true);
        OrderTable orderTable2 = new OrderTable(2L, 3, true);
        TableGroup tableGroup = new TableGroup();
        tableGroup.groupTables(Arrays.asList(orderTable1, orderTable2));

        //when,then
        assertThatThrownBy(() -> orderTable1.updateTableStatus(true))
            .isInstanceOf(TableUpdateStateException.class);
    }

    @DisplayName("주문상태가 계산완료가 아닌 주문이 있는 경우 업데이트 불가")
    @Test
    void updateEmpty_exception2() {
        //given
        OrderTable orderTable = new OrderTable(6, false);
        List<OrderLineItem> orderLineItems = Arrays.asList(
            new OrderLineItem(MenuTestFixtures.서비스군만두, 5)
        );

        //when
        Order order = new Order(orderTable, orderLineItems);

        //then
        assertThatThrownBy(() -> orderTable.updateTableStatus(true))
            .isInstanceOf(TableUpdateStateException.class);
    }

    @DisplayName("테이블 방문 손님 수 변경")
    @Test
    void changeNumberOfGuests() {
        //given
        OrderTable orderTable = new OrderTable(6, false);
        int changeNumber = 4;

        //when
        orderTable.changeNumberOfGuests(changeNumber);

        //then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(changeNumber);
    }

    @DisplayName("변경 요청 방문 손님 수는 0 이상이어야 한다.")
    @Test
    void changeNumberOfGuests_exception1() {
        //given
        OrderTable orderTable = new OrderTable(6, false);
        int changeNumber = -1;

        //when, then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(changeNumber))
            .isInstanceOf(TableChangeNumberOfGuestsException.class);
    }

    @DisplayName("주문종료 상태의 테이블은 방문손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_exception2() {
        //given
        OrderTable orderTable = new OrderTable(6, true);

        //when, then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(2))
            .isInstanceOf(TableChangeNumberOfGuestsException.class);
    }
}
