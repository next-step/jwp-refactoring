package kitchenpos.moduledomain.common;


import static java.util.Arrays.asList;

import java.util.ArrayList;
import kitchenpos.moduledomain.table.NumberOfGuests;
import kitchenpos.moduledomain.table.OrderTable;
import kitchenpos.moduledomain.table.OrderTableStatus;
import kitchenpos.moduledomain.table.OrderTables;


public class OrderTableFixture {

    public static OrderTable 첫번째_주문테이블() {
        return OrderTable.of(1L, new NumberOfGuests(1), OrderTableStatus.USE, new ArrayList<>());
    }

    public static OrderTable 두번째_주문테이블() {
        return OrderTable.of(2L, new NumberOfGuests(2), OrderTableStatus.USE, new ArrayList<>());
    }

    public static OrderTable 단체지정_첫번째_주문테이블() {
        return OrderTable.of(1L, new NumberOfGuests(1), OrderTableStatus.EMPTY,
            asList(OrderFixture.주문()));
    }

    public static OrderTable 단체지정_두번째_주문테이블() {
        return OrderTable.of(2L, new NumberOfGuests(2), OrderTableStatus.EMPTY,
            asList(OrderFixture.주문()));
    }

    public static OrderTable 단체지정_첫번째_계산완료() {
        return OrderTable.of(1L, new NumberOfGuests(1), OrderTableStatus.EMPTY,
            asList(OrderFixture.계산_완료()));
    }

    public static OrderTable 단체지정_두번째_계산완료() {
        return OrderTable.of(2L, new NumberOfGuests(2), OrderTableStatus.EMPTY,
            asList(OrderFixture.계산_완료()));
    }

    public static OrderTables 단체지정() {
        return OrderTables.of(asList(단체지정_첫번째_주문테이블(), 단체지정_두번째_주문테이블()));
    }

}
