package common;

import static common.OrderFixture.계산_완료;
import static common.OrderFixture.주문;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableStatus;
import kitchenpos.table.dto.OrderTableRequest;

public class OrderTableFixture {

    public static OrderTable 첫번째_주문테이블() {
        return OrderTable.of(1L, new NumberOfGuests(1), OrderTableStatus.USE, new ArrayList<>());
    }

    public static OrderTable 두번째_주문테이블() {
        return OrderTable.of(2L, new NumberOfGuests(2), OrderTableStatus.USE, new ArrayList<>());
    }

    public static OrderTable 단체지정_첫번째_주문테이블() {
        return OrderTable.of(1L, new NumberOfGuests(1), OrderTableStatus.EMPTY, asList(주문()));
    }

    public static OrderTable 단체지정_두번째_주문테이블() {
        return OrderTable.of(2L, new NumberOfGuests(2), OrderTableStatus.EMPTY, asList(주문()));
    }

    public static OrderTable 단체지정_첫번째_계산완료() {
        return OrderTable.of(1L, new NumberOfGuests(1), OrderTableStatus.EMPTY, asList(계산_완료()));
    }

    public static OrderTable 단체지정_두번째_계산완료() {
        return OrderTable.of(2L, new NumberOfGuests(2), OrderTableStatus.EMPTY, asList(계산_완료()));
    }

    public static OrderTableRequest from(OrderTable orderTable) {
        return new OrderTableRequest(orderTable.getNumberOfGuests(),
            orderTable.isEmpty());
    }
}
