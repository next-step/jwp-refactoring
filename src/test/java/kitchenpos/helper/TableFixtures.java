package kitchenpos.helper;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;

public class TableFixtures {

    public static OrderTableRequest 테이블_요청_만들기(Long id, Integer numberOfGuests, Boolean isEmpty) {
        return new OrderTableRequest(id, null, numberOfGuests, isEmpty);
    }

    public static OrderTableRequest 테이블_요청_만들기(Integer numberOfGuests, Boolean isEmpty) {
        return 테이블_요청_만들기(null, numberOfGuests, isEmpty);
    }

    public static OrderTableRequest 테이블_요청_만들기(Integer numberOfGuests) {
        return 테이블_요청_만들기(numberOfGuests, null);
    }

    public static OrderTableRequest 테이블_요청_만들기(Long id) {
        return 테이블_요청_만들기(id, null, null);
    }

    public static OrderTable 테이블_만들기(Integer numberOfGuests, Boolean isEmpty) {
        return 테이블_만들기(null, numberOfGuests, isEmpty);
    }

    public static OrderTable 테이블_만들기(Long id, Integer numberOfGuests, Boolean isEmpty) {
        return new OrderTable(id, numberOfGuests, isEmpty);
    }

}
