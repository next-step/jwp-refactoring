package kitchenpos.fixture;

import kitchenpos.domain.NumberOfGuest;
import kitchenpos.domain.OrderTable;

import java.util.Collections;

import static kitchenpos.fixture.TableGroupFixture.주문이_없는_테이블_그룹;

public class OrderTableFixture {
    public static OrderTable 미사용중인_테이블;
    public static OrderTable 미사용중인_테이블2;
    public static OrderTable 사용중인_1명_테이블;
    public static OrderTable 사용중인_2명_테이블;

    public static void cleanUp() {
        TableGroupFixture.cleanUp();

        미사용중인_테이블 = new OrderTable(1L, new NumberOfGuest(0), true);
        미사용중인_테이블2 = new OrderTable(2L, new NumberOfGuest(0), true);
        사용중인_1명_테이블 = new OrderTable(3L, 주문이_없는_테이블_그룹, Collections.emptyList(), new NumberOfGuest(1), false);
        사용중인_2명_테이블 = new OrderTable(4L, 주문이_없는_테이블_그룹, Collections.emptyList(), new NumberOfGuest(2), false);
    }
}
