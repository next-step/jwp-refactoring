package kitchenpos.helper;

import kitchenpos.table.domain.NumberOfGuest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableEmpty;
import kitchenpos.table.dto.OrderTableRequest;

public class TableFixtures {

    public static final OrderTable 주문_테이블 = 테이블_만들기(3, false);
    public static final OrderTable 빈_테이블 = 테이블_만들기(0, true);
    public static final OrderTableRequest 주문_테이블_요청 = 테이블_요청_만들기(3, false);
    public static final OrderTableRequest 빈_테이블_요청 = 테이블_요청_만들기(0, true);

    public static OrderTableRequest 테이블_요청_만들기(Long id, Integer numberOfGuests, Boolean isEmpty) {
        return new OrderTableRequest(id, null, numberOfGuests, isEmpty);
    }

    public static OrderTableRequest 테이블_요청_만들기(Integer numberOfGuests, Boolean isEmpty) {
        return 테이블_요청_만들기(null, numberOfGuests, isEmpty);
    }

    public static OrderTableRequest 테이블_요청_만들기(Integer numberOfGuests) {
        return 테이블_요청_만들기(null, numberOfGuests, null);
    }

    public static OrderTableRequest 테이블_요청_만들기(Boolean isEmpty) {
        return 테이블_요청_만들기(null,null, isEmpty);
    }

    public static OrderTableRequest 테이블_요청_만들기(Long id) {
        return 테이블_요청_만들기(id, null, null);
    }

    public static OrderTable 빈_테이블_만들기() {
        return 테이블_만들기(null, 0, true);
    }

    public static OrderTable 주문_테이블_만들기() {
        return 테이블_만들기(null, 1, false);
    }

    public static OrderTable 테이블_만들기(Integer numberOfGuests, Boolean isEmpty) {
        return 테이블_만들기(null, numberOfGuests, isEmpty);
    }

    public static OrderTable 테이블_만들기(Long id, Integer numberOfGuests, Boolean isEmpty) {
        return new OrderTable(id, new NumberOfGuest(numberOfGuests), new TableEmpty(isEmpty));
    }

}
