package kitchenpos.domain.table.fixture;

import kitchenpos.domain.table.domain.OrderTable;
import kitchenpos.domain.table_group.domain.TableGroup;
import kitchenpos.domain.table.dto.TableRequest;

public class OrderTableFixture {

    public static OrderTable 주문_테이블(int numberOfGuests, boolean empty) {
        return 주문_테이블(numberOfGuests, null, empty);
    }

    public static OrderTable 주문_테이블(int numberOfGuests, TableGroup tableGroup, boolean empty) {
        return new OrderTable(tableGroup, numberOfGuests, empty);
    }

    public static TableRequest 테이블_요청(int numberOfGuests, boolean empty) {
        return new TableRequest(numberOfGuests, empty);
    }
}
