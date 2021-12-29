package kitchenpos.fixture;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;

import java.util.List;

public class OrderTableFixture {

    public static OrderTable 생성(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public static OrderTable 생성(int numberOfGuests, boolean empty, List<Order> orders) {
        return new OrderTable(numberOfGuests, empty, orders);
    }

    public static OrderTable 단체지정_주문테이블() {
        OrderTable orderTable = 생성(1, false);
        orderTable.group(1L);
        return orderTable;
    }
}
