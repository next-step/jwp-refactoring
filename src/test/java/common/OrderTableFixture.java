package common;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable 첫번째_주문테이블() {

        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setTableGroupId(null);
        orderTable.setNumberOfGuests(1);
        orderTable.setEmpty(false);

        return orderTable;
    }

    public static OrderTable 두번째_주문테이블() {

        OrderTable orderTable = new OrderTable();
        orderTable.setId(2L);
        orderTable.setTableGroupId(null);
        orderTable.setNumberOfGuests(2);
        orderTable.setEmpty(false);

        return orderTable;
    }

    public static OrderTable 단체지정_첫번째_주문테이블() {

        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setTableGroupId(null);
        orderTable.setNumberOfGuests(1);
        orderTable.setEmpty(true);

        return orderTable;
    }

    public static OrderTable 단체지정_두번째_주문테이블() {

        OrderTable orderTable = new OrderTable();
        orderTable.setId(2L);
        orderTable.setTableGroupId(null);
        orderTable.setNumberOfGuests(2);
        orderTable.setEmpty(true);

        return orderTable;
    }
}
