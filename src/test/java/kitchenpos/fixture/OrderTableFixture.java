package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class OrderTableFixture {

    public static OrderTable 주문테이블_생성요청_데이터_생성(int numberOfGuests) {
//        return new OrderTable(null, null, numberOfGuests, false);
        return null;
    }

    public static OrderTable 주문테이블_데이터_생성(Long id, TableGroup tableGroup, int numberOfGuests, boolean isEmpty) {
        return new OrderTable(id, tableGroup, numberOfGuests, isEmpty);
    }

    public static OrderTable 주문테이블_비우기_데이터_생성(boolean isEmpty) {
//        return new OrderTable(null, null, 0, isEmpty);
        return null;
    }

    public static OrderTable 주문테이블_손님수변경_데이터_생성(int numberOfGuests) {
//        return new OrderTable(null, null, numberOfGuests, false);
        return null;
    }

}
