package kitchenpos.utils.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kitchenpos.domain.OrderTable;

public class OrderTableObjects {
    private final OrderTable orderTable1;
    private final OrderTable orderTable2;
    private final OrderTable orderTable3;
    private final OrderTable orderTable4;
    private final OrderTable orderTable5;
    private final OrderTable orderTable6;
    private final OrderTable orderTable7;
    private final OrderTable orderTable8;

    public OrderTableObjects() {
        orderTable1 = new OrderTable();
        orderTable2 = new OrderTable();
        orderTable3 = new OrderTable();
        orderTable4 = new OrderTable();
        orderTable5 = new OrderTable();
        orderTable6 = new OrderTable();
        orderTable7 = new OrderTable();
        orderTable8 = new OrderTable();

        orderTable1.setId(1L);
        orderTable1.setNumberOfGuests(0);
        orderTable1.setEmpty(true);
        orderTable2.setId(1L);
        orderTable2.setNumberOfGuests(0);
        orderTable2.setEmpty(true);
        orderTable3.setId(1L);
        orderTable3.setNumberOfGuests(0);
        orderTable3.setEmpty(true);
        orderTable4.setId(1L);
        orderTable4.setNumberOfGuests(0);
        orderTable4.setEmpty(true);
        orderTable5.setId(1L);
        orderTable5.setNumberOfGuests(0);
        orderTable5.setEmpty(true);
        orderTable6.setId(1L);
        orderTable6.setNumberOfGuests(0);
        orderTable6.setEmpty(true);
        orderTable7.setId(1L);
        orderTable7.setNumberOfGuests(0);
        orderTable7.setEmpty(true);
        orderTable8.setId(1L);
        orderTable8.setNumberOfGuests(0);
        orderTable8.setEmpty(true);
    }

    public OrderTable getOrderTable1() {
        return orderTable1;
    }

    public OrderTable getOrderTable2() {
        return orderTable2;
    }

    public OrderTable getOrderTable3() {
        return orderTable3;
    }

    public OrderTable getOrderTable4() {
        return orderTable4;
    }

    public OrderTable getOrderTable5() {
        return orderTable5;
    }

    public OrderTable getOrderTable6() {
        return orderTable6;
    }

    public OrderTable getOrderTable7() {
        return orderTable7;
    }

    public OrderTable getOrderTable8() {
        return orderTable8;
    }

    public List<OrderTable> getOrderTables() {
        return new ArrayList<>(Arrays.asList(orderTable1, orderTable2, orderTable3, orderTable4, orderTable5, orderTable6, orderTable7, orderTable8));
    }
}
