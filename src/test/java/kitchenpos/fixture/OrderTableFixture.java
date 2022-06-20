package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderTableFixture {

    public static OrderTable 주문테이블_생성요청_데이터_생성(int numberOfGuests) {
        return new OrderTable(null, null, numberOfGuests, false);
    }

    public static OrderTable 주문테이블_데이터_생성(Long id, Long tableGroupId, int numberOfGuests, boolean isEmpty) {
        return new OrderTable(id, tableGroupId, numberOfGuests, isEmpty);
    }

    public static OrderTable 주문테이블_비우기_데이터_생성(boolean isEmpty) {
        return new OrderTable(null, null, 0, isEmpty);
    }

    public static OrderTable 주문테이블_손님수변경_데이터_생성(int numberOfGuests) {
        return new OrderTable(null, null, numberOfGuests, false);
    }

    public static void 주문테이블_데이터_확인(OrderTable orderTable, Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        assertAll(
                () -> assertEquals(id, orderTable.getId()),
                () -> assertEquals(tableGroupId, orderTable.getTableGroupId()),
                () -> assertEquals(numberOfGuests, orderTable.getNumberOfGuests()),
                () -> assertEquals(empty, orderTable.isEmpty())
        );
    }
}
