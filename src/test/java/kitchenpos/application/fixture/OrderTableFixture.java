package kitchenpos.application.fixture;


import static kitchenpos.application.fixture.TableGroupFixture.단체지정;

import java.util.Arrays;
import kitchenpos.ordertable.domain.OrderTable;

public class OrderTableFixture {

    private OrderTableFixture() {
    }

    public static OrderTable 빈_테이블() {
        return OrderTable.of(0, true);
    }

    public static OrderTable 한명_주문테이블() {
        return OrderTable.of(1, false);
    }

    public static OrderTable 단체지정된_주문테이블() {
        OrderTable orderTable = OrderTable.of(0, true);
        orderTable.changeTableGroup(단체지정(Arrays.asList(빈_테이블(), 빈_테이블())));
        return orderTable;
    }
}
