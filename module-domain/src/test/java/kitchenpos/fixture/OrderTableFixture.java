package kitchenpos.fixture;

import kitchenpos.table.domain.*;
import kitchenpos.table.dto.*;

public class OrderTableFixture {
    public static final OrderTable 주문테이블_4명 = OrderTable.of(4, true);
    public static final OrderTable 주문테이블_6명 = OrderTable.of(6, true);

    public static final OrderTableRequest 주문테이블_4명_요청 = OrderTableRequest.of(주문테이블_4명.getNumberOfGuests(),
        주문테이블_4명.isEmpty());

    public static final OrderTableRequest 주문테이블_6명_요청 = OrderTableRequest.of(주문테이블_6명.getNumberOfGuests(),
        주문테이블_6명.isEmpty());
}
