package kitchenpos.table.dto;

import kitchenpos.order.dto.OrderTableRequest;

public class OrderTableRequestTest {

    public static OrderTableRequest 주문_테이블_생성_요청_객체_생성(int numberOfGuests, boolean empty) {
        return new OrderTableRequest.Builder()
                .numberOfGuests(numberOfGuests)
                .empty(empty)
                .build();
    }
}