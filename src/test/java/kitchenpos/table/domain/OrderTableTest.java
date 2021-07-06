package kitchenpos.table.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderTableTest {

    private List<OrderTable> orderTableList;
    private int numberOfGuests;
    private boolean empty;

    @BeforeEach
    public void setup() {
        orderTableList = new ArrayList<>();
        numberOfGuests = 1;
        empty = false;
    }

    @Test
    @DisplayName("주문 테이블을 생성 한다")
    public void createOrderTable() {
        //when
        OrderTable orderTable = new OrderTable(numberOfGuests, empty);

        //then
        assertThat(orderTable).isEqualTo(new OrderTable(numberOfGuests, empty));

    }

    @Test
    @DisplayName("주문 테이블 상태를 빈 테이블로 변경 한다")
    public void modifyOrderTableEmpty() {
        //given
        OrderTable orderTable = new OrderTable(numberOfGuests, empty);

        //when
        orderTable.changeEmpty(true);

        //then
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("주문 테이블 손님 수를 변경 한다")
    public void modifyOrderTableGuests() {
        //given
        OrderTable orderTable = new OrderTable(numberOfGuests, empty);

        //when
        orderTable.changeNumberOfGuests(3);

        //then
        assertThat(orderTable.numberOfGuests()).isEqualTo(3);

    }

    @Test
    @DisplayName("단체 지정 실패 - 주문 테이블이 empty가 아닐 경우")
    public void createOrderTableGroupFailByOnderTableIsNotEmpty() {
        // given
        orderTableList.add(new OrderTable(3L, 0, false));
        orderTableList.add(new OrderTable(6L, 0, false));
        OrderTables orderTables = new OrderTables(orderTableList);
        // when
        // then
        assertThrows(IllegalArgumentException.class, orderTables::validateOrderTables);
    }

    @Test
    @DisplayName("단체 지정 실패 - 이미 단체 지정이 된 테이블")
    public void createOrderTableGroupFailByAlreadyExistsTableGroup() {
        // given
        orderTableList.add(new OrderTable(9L, 0, false));
        orderTableList.add(new OrderTable(10L, 0, false));
        OrderTables orderTables = new OrderTables(orderTableList);
        // when
        // then
        assertThrows(IllegalArgumentException.class, orderTables::validateOrderTables);
    }

    @Test
    @DisplayName("단체 지정 실패 - 기존 데이터베이스에 존재하지 않는 테이블을 포함하여 단체 지정 할 경우")
    public void createOrderTableGroupFailByNotExistsTable() {
        // given
        orderTableList.add(new OrderTable(10L, 0, false));
        orderTableList.add(new OrderTable(11L, 0, false));
        OrderTables orderTables = new OrderTables(orderTableList);
        // when
        // then
        assertThrows(IllegalArgumentException.class, orderTables::validateOrderTables);
    }


}
