package kitchenpos.fixture;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.OrderTableRequest;

import java.util.List;

public class OrderTableFixture {

    public static OrderTableRequest 샘플_Request() {
        OrderTableRequest sample = new OrderTableRequest();
        sample.setEmpty(true);
        sample.setNumberOfGuests(0);
        return sample;
    }

    public static OrderTableRequest 생성_Request(int numberOfGuests, boolean empty) {
        OrderTableRequest orderTableRequest = new OrderTableRequest();
        orderTableRequest.setNumberOfGuests(numberOfGuests);
        orderTableRequest.setEmpty(empty);
        return orderTableRequest;
    }

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
