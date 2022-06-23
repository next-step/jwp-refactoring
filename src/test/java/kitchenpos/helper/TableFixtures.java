package kitchenpos.helper;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;

public class TableFixtures {

    public static OrderTableRequest 테이블_요청_만들기(Integer numberOfGuests, Boolean isEmpty){
        return new OrderTableRequest(null, null, numberOfGuests, isEmpty);
    }

    public static OrderTable 테이블_만들기(Integer numberOfGuests, Boolean isEmpty){
        return new OrderTable(null, numberOfGuests, isEmpty);
    }

}
