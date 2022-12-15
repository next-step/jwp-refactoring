package kitchenpos.table.fixture;

import kitchenpos.table.domain.OrderTable;

public class OrderTableFixture {
    public static OrderTable 빈_테이블(){
        return OrderTable.of(2, true);
    }
    public static OrderTable 채워진_테이블(){
        return OrderTable.of(2, false);
    }
}
