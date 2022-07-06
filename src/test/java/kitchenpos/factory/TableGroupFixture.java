package kitchenpos.factory;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public class TableGroupFixture {
    public static OrderTable 테이블_생성(Long id) {
        return new OrderTable(id, null, 2, false);
    }

    public static OrderTable 테이블_생성(Long id, Long tableGroupId, int numberOfGuests, boolean isEmpty) {
        return new OrderTable(id, tableGroupId, numberOfGuests, isEmpty);
    }
}
