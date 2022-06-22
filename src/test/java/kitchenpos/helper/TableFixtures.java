package kitchenpos.helper;

import kitchenpos.table.dto.OrderTableRequest;

public class TableFixtures {

    public static OrderTableRequest 테이블_요청_만들기(Integer numberOfGuests, Boolean isEmpty){
        return new OrderTableRequest(null, null, numberOfGuests, isEmpty);
    }

}
